import { useEffect, useMemo, useState } from "react";
import "./App.css";
import {
  createHabit,
  deleteHabit,
  getHabits,
  markHabit,
  markHabitOn,
  unmarkHabitOn,
  getMonthlyStats,
  getRangeStats,
  type HabitDto,
  type CreateHabitRequest,
} from "./lib/api";

type Tab = "habits" | "stats" | "calendar";

function App() {
  const [tab, setTab] = useState<Tab>("habits");
  return (
    <div className="app">
      <header>
        <h1>Habit Tracker</h1>
        <nav>
          <button
            onClick={() => setTab("habits")}
            className={tab === "habits" ? "active" : ""}
          >
            Habits
          </button>
          <button
            onClick={() => setTab("stats")}
            className={tab === "stats" ? "active" : ""}
          >
            Stats
          </button>
          <button
            onClick={() => setTab("calendar")}
            className={tab === "calendar" ? "active" : ""}
          >
            Calendar
          </button>
        </nav>
      </header>
      {tab === "habits" ? (
        <HabitsView />
      ) : tab === "stats" ? (
        <StatsView />
      ) : (
        <CalendarView />
      )}
    </div>
  );
}

function HabitsView() {
  const [habits, setHabits] = useState<HabitDto[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [form, setForm] = useState<CreateHabitRequest>({
    name: "",
    description: "",
    frequency: "daily",
  });

  const load = async () => {
    setLoading(true);
    setError(null);
    try {
      setHabits(await getHabits());
    } catch (e: any) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await createHabit(form);
      setForm({ name: "", description: "", frequency: "daily" });
      await load();
    } catch (e: any) {
      setError(e.message);
    }
  };

  const onDelete = async (id: number) => {
    // Optimistic UI update
    setHabits((prev) => prev.filter((h) => h.id !== id));
    try {
      await deleteHabit(id);
    } catch (e: any) {
      setError(e.message);
      await load();
    }
  };
  const onMark = async (id: number) => {
    await markHabit(id);
    await load();
  };
  const onMarkYesterday = async (id: number) => {
    const d = new Date();
    d.setDate(d.getDate() - 1);
    await markHabitOn(id, d.toISOString().slice(0, 10));
    await load();
  };

  return (
    <main>
      <section className="card">
        <h2>Create Habit</h2>
        <form onSubmit={submit} className="row">
          <input
            placeholder="Name"
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            required
          />
          <input
            placeholder="Description"
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
          />
          <select
            value={form.frequency}
            onChange={(e) =>
              setForm({ ...form, frequency: e.target.value as any })
            }
          >
            <option value="daily">Daily</option>
            <option value="weekly">Weekly</option>
            <option value="monthly">Monthly</option>
          </select>
          <button type="submit">Add</button>
        </form>
        {error && <p className="error">{error}</p>}
      </section>

      <section className="card">
        <h2>Habits</h2>
        {loading ? (
          <p>Loading...</p>
        ) : (
          <ul className="list">
            {habits.map((h) => (
              <li key={h.id} className="row">
                <div>
                  <strong>{h.name}</strong>
                  <div className="muted">{h.description}</div>
                  <div className="muted">Frequency: {h.frequency}</div>
                </div>
                <div className="row gap">
                  <button onClick={() => onMark(h.id)}>Mark Today</button>
                  <button onClick={() => onMarkYesterday(h.id)}>
                    Mark Yesterday
                  </button>
                  <button className="danger" onClick={() => onDelete(h.id)}>
                    Delete
                  </button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </section>
    </main>
  );
}

function StatsView() {
  const today = useMemo(() => new Date(), []);
  const [year, setYear] = useState(today.getFullYear());
  const [month, setMonth] = useState(today.getMonth() + 1);
  const [monthly, setMonthly] = useState<Record<string, number>>({});
  const [rangeStart, setRangeStart] = useState(() =>
    new Date(today.getFullYear(), today.getMonth(), 1)
      .toISOString()
      .slice(0, 10)
  );
  const [rangeEnd, setRangeEnd] = useState(() =>
    new Date(today.getFullYear(), today.getMonth() + 1, 0)
      .toISOString()
      .slice(0, 10)
  );
  const [rangeStats, setRangeStats] = useState<
    Record<string, { completions: number; possible: number; rate: number }>
  >({});

  const loadMonthly = async () =>
    setMonthly(await getMonthlyStats(year, month));
  const loadRange = async () =>
    setRangeStats(await getRangeStats(rangeStart, rangeEnd));

  useEffect(() => {
    loadMonthly();
  }, []);

  return (
    <main>
      <section className="card">
        <h2>Monthly Stats</h2>
        <div className="row">
          <input
            type="number"
            value={year}
            onChange={(e) => setYear(parseInt(e.target.value || "0"))}
          />
          <input
            type="number"
            min={1}
            max={12}
            value={month}
            onChange={(e) => setMonth(parseInt(e.target.value || "1"))}
          />
          <button onClick={loadMonthly}>Load</button>
        </div>
        <ul className="list">
          {Object.entries(monthly).map(([name, count]) => (
            <li key={name} className="row">
              <strong>{name}</strong>
              <span>{count}</span>
            </li>
          ))}
        </ul>
      </section>

      <section className="card">
        <h2>Range Stats</h2>
        <div className="row">
          <input
            type="date"
            value={rangeStart}
            onChange={(e) => setRangeStart(e.target.value)}
          />
          <input
            type="date"
            value={rangeEnd}
            onChange={(e) => setRangeEnd(e.target.value)}
          />
          <button onClick={loadRange}>Load</button>
        </div>
        <ul className="list">
          {Object.entries(rangeStats).map(([name, s]) => (
            <li key={name} className="row">
              <div>
                <strong>{name}</strong>
              </div>
              <div className="row gap">
                <span>Completions: {s.completions}</span>
                <span>Possible: {s.possible}</span>
                <span>Rate: {(s.rate * 100).toFixed(0)}%</span>
              </div>
            </li>
          ))}
        </ul>
      </section>
    </main>
  );
}

export default App;

function daysInMonth(year: number, month: number) {
  return new Date(year, month, 0).getDate();
}

// helper reserved for future use

function CalendarView() {
  const today = new Date();
  const [year, setYear] = useState(today.getFullYear());
  const [month, setMonth] = useState(today.getMonth() + 1); // 1-12
  const [habits, setHabits] = useState<HabitDto[]>([]);
  const [marked, setMarked] = useState<Record<number, Set<string>>>({}); // habitId -> set of ISO dates

  const load = async () => {
    const hs = await getHabits();
    setHabits(hs);
    const startIso = `${year}-${String(month).padStart(2, "0")}-01`;
    const endIso = `${year}-${String(month).padStart(2, "0")}-${String(
      daysInMonth(year, month)
    ).padStart(2, "0")}`;
    const byHabit: Record<number, Set<string>> = {};
    for (const h of hs) {
      const logs = (await fetch(
        `/api/habits/${h.id}/logs?start=${startIso}&end=${endIso}`
      ).then((r) => r.json())) as { id: number; date: string }[];
      byHabit[h.id] = new Set(logs.map((l) => l.date));
    }
    setMarked(byHabit);
  };

  useEffect(() => {
    load();
  }, [year, month]);

  const toggle = async (habitId: number, dateIso: string) => {
    const set = new Set(marked[habitId] ?? new Set<string>());
    const isMarked = set.has(dateIso);
    // Optimistic
    if (isMarked) set.delete(dateIso);
    else set.add(dateIso);
    setMarked((prev) => ({ ...prev, [habitId]: set }));
    try {
      if (isMarked) await unmarkHabitOn(habitId, dateIso);
      else await markHabitOn(habitId, dateIso);
    } catch {
      // revert on error
      if (isMarked) set.add(dateIso);
      else set.delete(dateIso);
      setMarked((prev) => ({ ...prev, [habitId]: set }));
    }
  };

  const days = Array.from(
    { length: daysInMonth(year, month) },
    (_, i) => i + 1
  );
  const monthLabel = `${year}-${String(month).padStart(2, "0")}`;

  return (
    <main>
      <section className="card">
        <h2>Calendar</h2>
        <div className="row">
          <input
            type="number"
            value={year}
            onChange={(e) => setYear(parseInt(e.target.value || "0"))}
          />
          <input
            type="number"
            min={1}
            max={12}
            value={month}
            onChange={(e) => setMonth(parseInt(e.target.value || "1"))}
          />
        </div>
      </section>

      <section className="card">
        <table className="calendar-table">
          <thead>
            <tr>
              <th style={{ textAlign: "left", padding: "4px 8px", width: 64 }}>
                Day
              </th>
              {habits.map((h) => (
                <th
                  key={h.id}
                  style={{ textAlign: "center", padding: "4px 0" }}
                  title={h.description}
                >
                  {h.name}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {days.map((d) => {
              const iso = `${monthLabel}-${String(d).padStart(2, "0")}`;
              return (
                <tr key={d}>
                  <td className="muted" style={{ padding: "4px 8px" }}>
                    {d}
                  </td>
                  {habits.map((h) => {
                    const checked = (marked[h.id] ?? new Set<string>()).has(
                      iso
                    );
                    return (
                      <td
                        key={h.id}
                        style={{ textAlign: "center", padding: "2px 0" }}
                      >
                        <input
                          type="checkbox"
                          checked={checked}
                          onChange={() => toggle(h.id, iso)}
                        />
                      </td>
                    );
                  })}
                </tr>
              );
            })}
          </tbody>
        </table>
      </section>
    </main>
  );
}
