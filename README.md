# ServiceHub - All-in-One Service Hub App

**The complete mobile solution for Ashcol Airconditioning Corporation's service management system.**

## Overview

ServiceHub is an all-in-one native Android application that serves **customers**, **employees (staff)**, and **administrators** - providing comprehensive mobile access to all aspects of Ashcol's service management ecosystem. The app connects seamlessly to the Ashcol Portal Laravel backend, enabling users to manage tickets, profiles, workloads, and business operations from anywhere.

### User Roles & Capabilities

#### 👤 **Customers**
- Create and track service tickets
- View ticket status and updates
- Add comments to tickets
- Manage profile and account settings
- Request new services
- View service history

#### 👷 **Employees (Staff)**
- View assigned tickets and workloads
- Update ticket status and priorities
- Add comments and updates to tickets
- Manage work schedule and availability
- View customer information
- Track assigned service requests

#### 👨‍💼 **Administrators**
- Full system access and management
- View all tickets across the organization
- Assign tickets to staff members
- Manage users, employees, and branches
- Access analytics and reporting
- Configure system settings
- Oversee workload distribution

## Current Status

### ✅ Completed Features
- ✅ User Authentication (Login/Logout)
- ✅ API Integration (Laravel ↔ Android)
- ✅ Token-based Authentication (Sanctum)
- ✅ Profile View (shows email)
- ✅ Basic Dashboard

## 🗺️ Roadmap

### Phase 1: Profile Enhancement (1-2 days) ⭐ HIGH PRIORITY
**Status:** Not Started

**Features:**
- View full user profile (name, email, role)
- Edit profile (update name, email)
- Change password
- Profile picture (optional)

**API Endpoints Needed:**
- `PUT /api/v1/profile` - Update profile
- `PUT /api/v1/profile/password` - Change password

**Android Features:**
- ProfileActivity with edit form
- Update name and email
- Change password functionality
- Better UI for profile display

---

### Phase 2: Ticket System (3-5 days) ⭐⭐ HIGH PRIORITY
**Status:** Not Started

**Features:**
- **For Customers:** List their own tickets, create new service requests
- **For Employees:** View assigned tickets, update status, manage workload
- **For Admins:** View all tickets, assign to staff, manage priorities
- Create new ticket with priority selection
- View ticket details with full history
- Add comments to tickets
- Update ticket status (role-based permissions)
- Filter tickets by status, priority, date
- Real-time ticket updates

**API Endpoints Needed:**
- `GET /api/v1/tickets` - List tickets
- `POST /api/v1/tickets` - Create ticket
- `GET /api/v1/tickets/{id}` - Get ticket details
- `PUT /api/v1/tickets/{id}` - Update ticket
- `POST /api/v1/tickets/{id}/comments` - Add comment
- `GET /api/v1/tickets/{id}/comments` - Get comments

**Android Features:**
- TicketsListActivity - RecyclerView with tickets
- CreateTicketActivity - Form to create ticket
- TicketDetailActivity - View ticket and comments
- AddCommentActivity/Dialog - Add comments

---

### Phase 3: Dashboard Improvement (1-2 days) ⭐ MEDIUM PRIORITY
**Status:** Not Started

**Features:**
- **Customer Dashboard:** My Tickets count, Recent activity, Quick service request
- **Employee Dashboard:** Assigned tickets, Today's workload, Upcoming tasks, Availability status
- **Admin Dashboard:** System overview, All tickets, Staff workload, Branch statistics, User management
- Welcome message with user name and role
- Statistics cards (role-specific metrics)
- Quick action buttons (role-based)
- Recent activity feed
- Role-based dashboard with customized widgets

---

### Phase 4: UI/UX Enhancements (2-3 days) ⭐ MEDIUM PRIORITY
**Status:** Not Started

**Improvements:**
- Material Design 3 components
- Loading indicators (ProgressBar, Shimmer effect)
- Pull-to-refresh for lists
- Empty states (no tickets, no data)
- Error states with retry buttons
- Better navigation (Bottom Navigation, Navigation Drawer)
- Better color scheme and icons

---

### Phase 5: Search & Filter ⭐ MEDIUM PRIORITY
**Status:** Not Started

**Features:**
- Search tickets by title/description
- Filter tickets by status
- Sort tickets (date, status, priority)

---

### Phase 6: Notifications ⭐ LOW PRIORITY
**Status:** Not Started

**Features:**
- Push notifications for ticket updates
- In-app notifications
- Notification settings

**Requires:**
- Firebase Cloud Messaging (FCM)
- Laravel notification system

---

### Phase 7: Offline Support ⭐ LOW PRIORITY (Advanced)
**Status:** Not Started

**Features:**
- Cache data locally (Room Database)
- Sync when online
- Offline mode indicator

---

### Phase 8: Workload Management (For Employees & Admins) ⭐ MEDIUM PRIORITY
**Status:** Not Started

**Features:**
- **For Employees:** View assigned workloads, update availability, calendar view
- **For Admins:** Assign tickets to staff, manage schedules, workload distribution
- Workload calendar and scheduling
- Today/Upcoming/Overdue views
- Staff availability management
- Workload assignment interface

---

### Phase 9: Employee & Branch Management (For Admins) ⭐ LOW PRIORITY
**Status:** Not Started

**Features:**
- Employee roster management
- Branch management and assignment
- Skills and position tracking
- Link employees to users
- Branch-based ticket filtering

---

## 🛠️ Technical Improvements (Future)

### Architecture Enhancements
- [ ] Add Repository Pattern (UserRepository, TicketRepository)
- [ ] Add ViewModel (Android Architecture Components)
- [ ] Better State Management (sealed classes for state)
- [ ] Add Dependency Injection (Hilt or Dagger)

---

## 📝 API Endpoints Status

### Profile:
- ✅ `GET /api/v1/user` - Already exists
- ❌ `PUT /api/v1/profile` - Need to create
- ❌ `PUT /api/v1/profile/password` - Need to create

### Tickets:
- ❌ `GET /api/v1/tickets` - Need to create
- ❌ `POST /api/v1/tickets` - Need to create
- ❌ `GET /api/v1/tickets/{id}` - Need to create
- ❌ `PUT /api/v1/tickets/{id}` - Need to create
- ❌ `POST /api/v1/tickets/{id}/comments` - Need to create

---

## 🚀 Quick Wins (Easy to Implement)

1. **Show User Name in Dashboard** - Display user name instead of just email
2. **Add Logout Button to Dashboard** - Quick access to logout
3. **Display User Role** - Show user role in profile
4. **Improve Error Handling** - Show specific error messages with retry functionality

---

## 📱 Next Recommended Feature

**Start with Profile Enhancement** - Quick to implement (1-2 days), improves user experience, and provides good practice for API updates.

**Then: Ticket System** - Most valuable feature, backend already ready, demonstrates full CRUD operations.

---

## Setup

### Prerequisites
- Android Studio
- Java/Kotlin development environment
- Laravel backend running (see Ashcol Portal README)

### Configuration
1. Update API base URL in `app/src/main/java/hans/ph/api/ApiClient.java`
2. For physical device testing, use your computer's IP address instead of `localhost`
3. Ensure phone and computer are on the same Wi-Fi network

---

## Contributing

Branch naming: `feature/<name>` • Small, descriptive commits • Open PRs for review.

---

## License

This project uses Android (Apache 2.0). See [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).
