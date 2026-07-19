# Tractus - A Topic-Based Community Platform

> **Status:** Academic-Safe Development Mode
> **Rule:** Done is better than perfect. Focus on Micro-Tasks.

---

## 📌 PROJECT MEMORY / NEXT ACTIONS
<!-- Academic වැඩ ඉවර වෙලා ආපහු එද්දී කරන්න ඕන පොඩිම දේ මෙතන Update කරන්න -->
- [ ] CURRENT FOCUS: Initialize the projects and database schema.

---

## 🗺️ MVP ROADMAP

### 📁 Phase 1: Database & Backend Architecture (Spring Boot)
- [x] Initialize Spring Boot Web API Project
- [x] Setup JPA/Hibernate with PostgreSQL
- [x] Design Core Schema: Users, Spaces (Topics), Threads, Replies
- [x] Implement JWT Authentication Endpoints (Register / Login)

### 💻 Phase 2: Frontend Core UI (React / Vite)
- [ ] Setup Frontend Project Structure & State Management
- [ ] Design Minimalist Light/Dark Theme Layout
- [ ] Create Login & Registration UI Forms
- [ ] Build Main Dashboard Sidebar with Spaces/Topics list

### 🔄 Phase 3: Core Community Interactions (The Feed)
- [ ] Create API Endpoints for Creating a Thread & Fetching Feed by Topic
- [ ] Integrate Frontend View with the Thread Feed
- [ ] Add Simple Reply / Comment System under Threads
- [ ] Implement Upvote/Like Toggle Functionality


backend
│
├── BackendApplication.java    (The main file that starts the app - already exists)
│
├── controllers/               (Layer 1: The Traffic Cops/ API endpoints)
├── services/                  (Layer 2: The Brains/Business Logic)
├── repositories/              (Layer 3: The Database Access)
├── models/                    (Layer 4: The Data Shapes - you started this!)
├── dtos/                      (Layer 5: Data Transfer Objects)
└── config/                    (Layer 6: Application & Security configurations)

---

## 🏗️ Frontend Architecture (Phase 2)

**Tech Stack:** React (Vite), TypeScript, React Router, Axios, Vanilla CSS.

```text
tractus_web/
├── src/
│   ├── assets/        # Images, SVG icons
│   ├── components/    # Reusable UI components (Buttons, Cards, Inputs)
│   ├── context/       # AuthContext for global state (JWT token)
│   ├── layouts/       # MainDashboardLayout (Sidebar + Main Content area)
│   ├── pages/         # Smart components (LoginPage, FeedPage, ThreadPage)
│   ├── services/      # Axios API calls (auth.service.ts, space.service.ts)
│   ├── types/         # TypeScript interfaces mapping to backend DTOs
│   ├── App.tsx        # React Router configuration
│   └── index.css      # Global Design System (Colors, Typography)
```
