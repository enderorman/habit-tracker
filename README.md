# Habit Tracker

A full-stack habit tracking application built with Spring Boot (backend) and React + TypeScript (frontend).

## Features

- ✅ Create, edit, and delete habits
- ✅ Track habits with different frequencies (daily, weekly, monthly)
- ✅ Calendar view with checkboxes for habit tracking
- ✅ Statistics and progress tracking
- ✅ Modern, responsive UI
- ✅ RESTful API backend

## Tech Stack

### Backend
- **Java 21** with Spring Boot 3.x
- **PostgreSQL** database
- **Spring Data JPA** for data persistence
- **Maven** for dependency management

### Frontend
- **React 18** with TypeScript
- **Vite** for fast development and building
- **CSS** for styling (no external UI libraries)

## Prerequisites

- Java 21 (JDK)
- Node.js 18+ and npm
- PostgreSQL 12+ (or Docker)

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/enderorman/habit-tracker.git
cd habit-tracker
```

### 2. Set Up Database

#### Option A: Using Docker (Recommended)

```bash
docker run --name habit-db -d -p 5432:5432 \
  -e POSTGRES_USER=enderorman \
  -e POSTGRES_PASSWORD=art666312 \
  -e POSTGRES_DB=habit_tracker \
  postgres:16
```

#### Option B: Local PostgreSQL

1. Install PostgreSQL on your system
2. Create a database named `habit_tracker`
3. Update database credentials in `src/main/resources/application.properties` if needed

### 3. Run the Backend

```bash
# From the project root
./mvnw spring-boot:run
```

The backend will start on `http://localhost:8080`

**API Endpoints:**
- `GET /api/habits` - List all habits
- `POST /api/habits` - Create a new habit
- `DELETE /api/habits/{id}` - Delete a habit
- `POST /api/habits/{id}/mark` - Mark habit as completed
- `POST /api/habits/{id}/unmark` - Unmark habit
- `GET /api/habits/{id}/logs` - Get habit logs
- `GET /api/stats` - Get statistics

### 4. Run the Frontend

```bash
# From the project root
cd frontend
npm ci
npm run dev
```

The frontend will start on `http://localhost:5173`

### 5. Access the Application

Open your browser and navigate to `http://localhost:5173`

## Development

### Backend Development

The backend uses Spring Boot with the following structure:

```
src/main/java/com/tracker/habittracker/
├── controller/     # REST API endpoints
├── service/        # Business logic
├── repository/     # Data access layer
├── model/          # Entity classes
└── dto/           # Data transfer objects
```

**Key files:**
- `HabitController.java` - REST API endpoints
- `HabitService.java` - Business logic for habits
- `HabitLogService.java` - Business logic for habit tracking
- `Habit.java` - Main entity model
- `Frequency.java` - Enum for habit frequencies

### Frontend Development

The frontend uses React with TypeScript:

```
frontend/src/
├── App.tsx         # Main application component
├── App.css         # Global styles
└── lib/
    └── api.ts      # API client functions
```

**Key features:**
- Tabbed interface (Habits, Calendar, Stats)
- Optimistic UI updates
- Responsive design
- Calendar view with habit tracking

### Database Schema

**Habits Table:**
- `id` (Primary Key)
- `name` (VARCHAR)
- `description` (TEXT)
- `frequency` (ENUM: DAILY, WEEKLY, MONTHLY)
- `created_at` (TIMESTAMP)

**Habit Logs Table:**
- `id` (Primary Key)
- `habit_id` (Foreign Key)
- `completed_date` (DATE)
- `created_at` (TIMESTAMP)

## Configuration

### Backend Configuration

Edit `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/habit_tracker
spring.datasource.username=enderorman
spring.datasource.password=art666312

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# CORS (for frontend)
app.cors.allowed-origins=http://localhost:5173
```

### Frontend Configuration

The frontend automatically proxies API requests to the backend during development. For production, set the `VITE_API_BASE` environment variable.

## Testing

### Backend Tests

```bash
./mvnw test
```

### Frontend Tests

```bash
cd frontend
npm test
```

## Building for Production

### Backend

```bash
./mvnw clean package
```

The JAR file will be created in `target/habittracker-0.0.1-SNAPSHOT.jar`

### Frontend

```bash
cd frontend
npm run build
```

The built files will be in `frontend/dist/`

## Deployment

### Backend Deployment

1. **Render (Free):**
   - Connect your GitHub repository
   - Set build command: `./mvnw clean package`
   - Set start command: `java -jar target/habittracker-0.0.1-SNAPSHOT.jar`
   - Add environment variables for database and CORS

2. **Local with Cloudflare Tunnel:**
   ```bash
   # Install cloudflared
   brew install cloudflare/cloudflare/cloudflared
   
   # Start tunnel
   cloudflared tunnel --url http://localhost:8080
   ```

### Frontend Deployment

1. **Netlify (Free):**
   - Connect your GitHub repository
   - Set build directory: `frontend`
   - Set publish directory: `frontend/dist`
   - Add environment variable: `VITE_API_BASE=https://your-backend-url`

## Troubleshooting

### Common Issues

1. **Database Connection Error:**
   - Ensure PostgreSQL is running
   - Check database credentials in `application.properties`
   - Verify database `habit_tracker` exists

2. **Frontend Can't Connect to Backend:**
   - Ensure backend is running on port 8080
   - Check CORS configuration
   - Verify Vite proxy settings in `frontend/vite.config.ts`

3. **Port Already in Use:**
   - Change backend port: `./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=8081"`
   - Change frontend port: `npm run dev -- --port 3000`

### Logs

- Backend logs: Check terminal where `./mvnw spring-boot:run` is running
- Frontend logs: Check browser DevTools Console
- Database logs: Check PostgreSQL logs or Docker container logs

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is open source and available under the [MIT License](LICENSE).

## Support

If you encounter any issues or have questions, please open an issue on GitHub.
