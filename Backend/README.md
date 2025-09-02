# Online Program Allocation System (Complete Full-Stack Application)

## Overview

This project provides a complete full-stack system for allocating programs to users. The application allows administrators to manage programs (CRUD operations) and enables users to select and enroll in available training programs. It now includes a modern React frontend with Vite for fast development.

## Features

- **Admin Capabilities:**
  - Create, Read, Update, and Delete (CRUD) programs
  - Manage user enrollments
  - View allocation statistics
- **User Capabilities:**
  - Browse available programs
  - Select and enroll in programs
- **Modern Frontend:**
  - React 18 with Vite for fast development
  - Responsive design with modern UI components
  - Real-time data management
  - User-friendly interface for all operations

## Technology Stack

### Backend
- **Framework:** Spring Boot (Java)
- **Database:** PostgreSQL
- **Java Version:** 17
- **API:** RESTful APIs with CORS support

### Frontend
- **Framework:** React 18
- **Build Tool:** Vite
- **HTTP Client:** Axios
- **Routing:** React Router DOM
- **Icons:** Lucide React
- **Styling:** Modern CSS with CSS variables

## Project Structure

```
├── OnlineTrainingProgram/          # Backend Spring Boot application
│   └── OnlineTrainingProgram/
│       ├── src/main/java/         # Java source code
│       └── pom.xml                # Maven configuration
├── frontend/                      # React frontend application
│   ├── src/
│   │   ├── components/           # React components
│   │   ├── services/             # API service layer
│   │   ├── App.jsx               # Main app component
│   │   └── main.jsx              # Entry point
│   ├── package.json              # Node.js dependencies
│   └── vite.config.js            # Vite configuration
└── README.md                     # This file
```

## Getting Started

### Prerequisites

- Java 17 or higher
- PostgreSQL database
- Node.js 16+ and npm (for frontend)

### Backend Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/amanworld8040/Online-Program-Allocatin-System-complete-Backend.git
   cd Online-Program-Allocatin-System-complete-Backend
   ```

2. **Set Up Database**
   - Ensure PostgreSQL is installed and running
   - Create a database named `OnlineTP`
   - Update credentials in `OnlineTrainingProgram/OnlineTrainingProgram/src/main/resources/application.properties`

3. **Build and Run Backend**
   ```bash
   cd OnlineTrainingProgram/OnlineTrainingProgram
   ./mvnw spring-boot:run
   ```
   or
   ```bash
   mvn spring-boot:run
   ```
   
   The backend will start on `http://localhost:8090`

### Frontend Setup

1. **Navigate to Frontend Directory**
   ```bash
   cd frontend
   ```

2. **Install Dependencies**
   ```bash
   npm install
   ```

3. **Start Development Server**
   ```bash
   npm run dev
   ```
   
   The frontend will start on `http://localhost:5173`

4. **Build for Production**
   ```bash
   npm run build
   ```

## API Endpoints

The backend provides the following REST API endpoints:

### Users
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create/update user
- `DELETE /api/users/{id}` - Delete user

### Training Programs
- `GET /api/training` - Get all training programs
- `GET /api/training/{id}` - Get training program by ID
- `POST /api/training` - Create/update training program
- `DELETE /api/training/{id}` - Delete training program

### Allocations
- `GET /api/allocations` - Get all allocations
- `GET /api/allocations/{id}` - Get allocation by ID
- `POST /api/allocations` - Create allocation
- `DELETE /api/allocations/{id}` - Delete allocation

## Frontend Features

### Pages and Components

1. **Home Page** - Dashboard with navigation and quick stats
2. **User Management** - Create, edit, and delete users with role management
3. **Training Management** - Manage training programs with status tracking
4. **Allocation Management** - Assign programs to users and track enrollments

### Key Features

- **Responsive Design** - Works on desktop, tablet, and mobile devices
- **Modern UI** - Clean, professional interface with intuitive navigation
- **Real-time Updates** - Automatic data refresh after operations
- **Form Validation** - Client-side validation for all forms
- **Error Handling** - User-friendly error messages and loading states
- **CORS Support** - Properly configured for cross-origin requests

## Development

### Running in Development Mode

1. **Backend Development**
   ```bash
   cd OnlineTrainingProgram/OnlineTrainingProgram
   ./mvnw spring-boot:run
   ```

2. **Frontend Development**
   ```bash
   cd frontend
   npm run dev
   ```

Both servers will auto-reload when you make changes to the code.

### Building for Production

1. **Backend**
   ```bash
   cd OnlineTrainingProgram/OnlineTrainingProgram
   ./mvnw clean package
   ```

2. **Frontend**
   ```bash
   cd frontend
   npm run build
   ```

## Usage Instructions

1. **Start both backend and frontend servers**
2. **Open your browser to `http://localhost:5173`**
3. **Navigate through the application:**
   - **Home** - View dashboard and navigate to different sections
   - **Users** - Manage user accounts (create admins and regular users)
   - **Training Programs** - Create and manage training courses
   - **Allocations** - Assign programs to users

## Contribution

This project now includes a complete frontend implementation. Contributions are welcome! Here's how you can contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines

- Follow existing code style and patterns
- Add appropriate error handling
- Test your changes with both frontend and backend
- Update documentation as needed

## License

This project is open-source. Feel free to use and modify.

## Contact

For queries or issues, please open a GitHub Issue or Pull Request.
