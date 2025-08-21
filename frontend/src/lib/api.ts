export type Frequency = "daily" | "weekly" | "monthly";

export interface HabitDto {
  id: number;
  name: string;
  description: string;
  frequency: "DAILY" | "WEEKLY" | "MONTHLY";
}

export interface CreateHabitRequest {
  name: string;
  description: string;
  frequency: Frequency;
}

const jsonHeaders = { "Content-Type": "application/json" } as const;
const API_BASE = import.meta.env.VITE_API_BASE || "";

export async function getHabits(): Promise<HabitDto[]> {
  const res = await fetch(`${API_BASE}/api/habits`);
  if (!res.ok) throw new Error("Failed to fetch habits");
  return res.json();
}

export async function createHabit(
  payload: CreateHabitRequest
): Promise<HabitDto> {
  const res = await fetch(`${API_BASE}/api/habits`, {
    method: "POST",
    headers: jsonHeaders,
    body: JSON.stringify(payload),
  });
  if (!res.ok) throw new Error("Failed to create habit");
  return res.json();
}

export async function deleteHabit(id: number): Promise<void> {
  const res = await fetch(`${API_BASE}/api/habits/${id}`, { method: "DELETE" });
  if (!res.ok) throw new Error("Failed to delete habit");
}

export async function markHabit(id: number): Promise<void> {
  const res = await fetch(`${API_BASE}/api/habits/${id}/mark`, {
    method: "POST",
  });
  if (!res.ok) throw new Error("Failed to mark habit");
}

export async function markHabitOn(id: number, date: string): Promise<void> {
  const res = await fetch(
    `${API_BASE}/api/habits/${id}/markOn?date=${encodeURIComponent(date)}`,
    { method: "POST" }
  );
  if (!res.ok) throw new Error("Failed to mark habit on date");
}

export async function unmarkHabitOn(id: number, date: string): Promise<void> {
  const res = await fetch(
    `${API_BASE}/api/habits/${id}/unmarkOn?date=${encodeURIComponent(date)}`,
    { method: "DELETE" }
  );
  if (!res.ok) throw new Error("Failed to unmark habit on date");
}

export async function getMonthlyStats(
  year: number,
  month: number
): Promise<Record<string, number>> {
  const res = await fetch(
    `${API_BASE}/api/habits/stats?year=${year}&month=${month}`
  );
  if (!res.ok) throw new Error("Failed to fetch monthly stats");
  return res.json();
}

export async function getRangeStats(
  start: string,
  end: string
): Promise<
  Record<string, { completions: number; possible: number; rate: number }>
> {
  const res = await fetch(
    `${API_BASE}/api/habits/stats/range?start=${encodeURIComponent(
      start
    )}&end=${encodeURIComponent(end)}`
  );
  if (!res.ok) throw new Error("Failed to fetch range stats");
  return res.json();
}

export interface HabitLogDto {
  id: number;
  date: string;
}

export async function getHabitLogs(
  id: number,
  start: string,
  end: string
): Promise<HabitLogDto[]> {
  const res = await fetch(
    `${API_BASE}/api/habits/${id}/logs?start=${encodeURIComponent(
      start
    )}&end=${encodeURIComponent(end)}`
  );
  if (!res.ok) throw new Error("Failed to fetch habit logs");
  return res.json();
}
