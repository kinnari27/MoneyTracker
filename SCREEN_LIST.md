# MoneyTracker - Screen List & UI Overview

This document provides a comprehensive list of all screens implemented in the MoneyTracker application along with their UI descriptions and actual screenshots.

---

## 1. Authentication & Onboarding

### Splash Screen
Initial loading screen featuring a pulsing currency logo (₹) and a multi-color gradient background (Green/Teal). Includes bouncing loading dots and version info.
**UI Components**: Pulsing ring animation, infinite transitions, version badge.

![Splash Screen](screenshorts/splash.png)

### Login Screen
The entry point for existing users. Features a deep green gradient header with decorative "blobs" and a clean, rounded card containing the login form.
**UI Components**: Google Sign-In button, email/password fields with icons, error banners.

![Login Screen](screenshorts/login.png)

### Register Screen (Multi-step)
A multi-step registration process (Basic Info -> Personalization -> Security) with a linear progress indicator at the top.
**UI Components**: Animated step transitions, custom currency picker, password strength bar.

#### Step 1: Basic Info
![Register Step 1](screenshorts/signup1.png)

#### Step 2: Personalization
![Register Step 2](screenshorts/signup2.png)

#### Step 3: Security
![Register Step 3](screenshorts/signup3.png)

### Forgot Password Screen
A dedicated screen to request password reset links, sharing the same consistent branding (gradient header and overlapping card) as the Login screen.
**UI Components**: Email input field, success/error feedback cards.

*(Screenshot coming soon)*

---

## 2. Core Dashboard (Bottom Navigation)

### Home Screen
The main dashboard giving a quick overview of the user's financial status.
**UI Components**: Balance card, recent transactions list, spending vs budget overview.

![Home Screen](screenshorts/home.png)

### History Screen
A comprehensive, searchable, and filterable list of all financial records.
**UI Components**: Search bar, category filters, swipe-to-delete (if implemented), transaction detail items.

![History Screen](screenshorts/transaction.png)

### Add Transaction
A detailed form for recording income, expenses, FD, and RD. The UI dynamically adjusts based on the selected type.
**UI Components**: Numeric keypad input, category icon grid, investment-specific fields (Interest Rate, Maturity Date).

#### Expense Type
![Add Expense](screenshorts/add_transaction1.png)

#### FD Type
![Add FD](screenshorts/add_transaction2.png)

#### RD Type
![Add RD](screenshorts/add_transaction3.png)

### Analytics Screen
Data visualization for spending patterns and budget tracking.
**UI Components**: Summary cards (Income/Spent/Invested), Money Flow stacked bar, category breakdown with progress bars.

![Analytics Screen](screenshorts/analytics.png)

### Profile Screen
The central hub for user account and app-wide settings.
**UI Components**: User profile summary, settings navigation menu, sign-out button.

![Profile Screen](screenshorts/profile.png)

---

## 3. Transactions

### Edit Transaction
Modification interface for existing financial entries, pre-filled with existing data.
**UI Components**: Delete action, update button, full form validation.

*(Uses same layout as Add Transaction)*

---

## 4. Settings & Personalization

### Edit Profile
Interface to update personal information like name, occupation, and financial goals.
**UI Components**: Form fields, save action.

![Edit Profile](screenshorts/editProfile.png)

### Notifications
Management of app alerts and reminders.
**UI Components**: Toggle switches for different notification types.

![Notifications](screenshorts/notification.png)

### Currency
Preference selector for financial reporting across the application.
**UI Components**: List of supported currencies with symbols.

![Currency](screenshorts/currency.png)

### Biometric Lock
Security settings for enabling fingerprint or face unlock for app access.
**UI Components**: Status toggle, setup instructions.

![Biometric Lock](screenshorts/biometric_lock.png)

### Change PIN
Update the security PIN used for app access.
**UI Components**: Secure PIN entry fields.

![Change PIN](screenshorts/change_pin.png)

---
*Last updated: April 2026*
