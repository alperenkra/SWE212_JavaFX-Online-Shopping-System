# 🛒 Online Shopping Management System

A desktop application designed to manage everyday retail operations. Built with a focus on relational database management, this project seamlessly connects an intuitive user interface with a robust PostgreSQL backend.

## 🚀 Technologies Used
* **Frontend:** JavaFX 25, FXML, SceneBuilder
* **Backend:** Java 21, JDBC
* **Database:** PostgreSQL (Relational tables with Primary/Foreign Keys)

## ✨ Key Features
* **Customer Management:** Full CRUD operations with instant UI validation.
* **Product Inventory:** Secure price and stock data handling.
* **Order Processing (JOINs):** Connects Customers and Products to generate detailed order records dynamically.
* **Exception Handling:** Prevents crashes gracefully with intuitive alert dialogs for missing or incorrect data inputs.

## 🛠️ Setup and Run
1. Clone the repository.
2. Run the provided SQL script to construct the `Customers`, `Products`, and `Orders` tables.
3. Update the `DatabaseConnection.java` with your PostgreSQL credentials.
4. Run via the `Launcher.java` class to bypass Java module restrictions smoothly.
