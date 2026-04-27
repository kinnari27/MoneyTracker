# MoneyTracker

MoneyTracker is a comprehensive Android application designed to help users manage their finances effectively. It allows for tracking income, expenses, and investments with advanced features like maturity calculations for FDs and RDs, and budget management.

## 🚀 Features

### 1. Authentication & Security
*   **User Accounts**: Secure Login and Registration system.
*   **Forgot Password**: Recovery options for user accounts.
*   **Biometric Lock**: Enhanced security using fingerprint or face recognition.
*   **PIN Protection**: Option to set and change a secure PIN for app access.

### 2. Dashboard & Overview
*   **Financial Summary**: At-a-glance view of your current financial status (Balance, Income, Expenses).
*   **Recent Activity**: Quick access to latest transactions.

### 3. Transaction Management
*   **Income & Expense Tracking**: Easily add, edit, or delete daily transactions.
*   **Advanced Investment Tracking**:
    *   **Fixed Deposits (FD)**: Calculate maturity amounts based on interest rates and tenure.
    *   **Recurring Deposits (RD)**: Automated maturity calculations for monthly installments.
*   **Recurring Transactions**: Support for marking transactions as recurring.
*   **Detailed Notes**: Attach memos to transactions for better record-keeping.
*   **Custom Dates**: Select specific dates for any transaction.

### 4. Budget Management
*   **Set Monthly Budgets**: Define spending limits for different categories.
*   **Progress Tracking**: Real-time monitoring of budget consumption versus spending.
*   **Overspending Alerts**: Visual indicators for budget status.

### 5. History & Analysis
*   **Transaction History**: A complete, searchable list of all financial records.
*   **Search & Filter**: Find transactions by category, type (Income/Expense/Investment), or note content.
*   **Analytics**: Visual charts (Weekly, Monthly, Yearly) to analyze spending patterns and income trends.

### 6. Personalized Settings
*   **Profile Management**: Update personal information and profile picture.
*   **Currency Support**: Select your preferred currency for display.
*   **Notifications**: Customizable alerts and reminders.

## 🛠 Tech Stack

*   **Language**: Kotlin
*   **UI Framework**: Jetpack Compose (Declarative UI)
*   **Architecture**: MVVM (Model-View-ViewModel) with Clean Architecture principles.
*   **Dependency Injection**: Hilt
*   **Local Database**: Room persistence library.
*   **Navigation**: Jetpack Navigation Compose.
*   **Asynchronous Programming**: Kotlin Coroutines & Flow.

## 🏗 Project Structure

*   `data`: Implementation of repositories and local database (Room).
*   `domain`: Business logic, entities, and UseCases.
*   `presentation`: UI components (Compose), ViewModels, and Navigation.
    *   `screen`: Individual screens like Home, Analytics, Budget, Settings, etc.
    *   `navigation`: NavGraph and Route definitions.

---

Built with ❤️ for better financial health.
