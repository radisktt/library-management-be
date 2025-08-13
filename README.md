Library Management Backend
library_management_be is a Spring Boot-based backend application for managing a library system. It supports functionalities such as borrowing/returning books, managing users, books, categories, publishers, fines, uploading user avatars, and scheduling overdue notifications. The application uses JWT for authentication, MySQL for data persistence, Redis for caching, and integrates with email notifications.
Features

User Management: Register, login, and update user information (including avatar upload).
Book Loan Management: Borrow/return books, extend due dates, and pay fines.
Authentication: JWT-based authentication with userId and role-based access control (USER, ADMIN).
Scheduling: Automated tasks to check overdue loans and send email notifications.
Logging: Comprehensive logging to info.log, debug.log, error.log, and scheduler.log.
Environment Configuration

Technologies

Spring Boot: 3.x
MySQL: Database for storing users, books, loans, and fines.
Redis: Caching for improved performance.
Apache POI: For generating Excel invoices.
Spring Security & JWT: For authentication and authorization.
Logback: For logging configuration.
Maven: Dependency management and build tool.

Prerequisites

Java: 17 or higher
Maven: 3.8.x or higher
MySQL: 8.x
Redis: 6.x or higher
SMTP Server: Gmail or any SMTP service for email notifications
Postman: For testing APIs

Setup Instructions
1. Clone the Repository
2. Configure Environment Variables
3. Database Setup
Create a MySQL database named libman:
CREATE DATABASE libman;
Initialize the schema and sample data by running src/main/resources/data.sql:
INSERT INTO roles (id, name) VALUES (1, 'USER'), (2, 'ADMIN');
4. Build and Run
Build the project:
./mvnw clean package
Run the application:

The application will be available at http://localhost:8080.
5. API Endpoints
   Below are the main API endpoints. Use Postman to test them.
   Authentication: /api/auth/
Register
URL: POST /api/auth/register
Body:{
    "name": "ExampleName",
    "email": "user@example.com",
    "password": "examplepassword",
    "confirmPassword": "examplepassword"
}

Response:{
    "status": "sucess",
    "message": "Account created successfully",
    "data": "Email user@example.com is created"
}

Verify account
URL: GET /api/auth/verify?token={authToken}
Response:{
   "status":"success",
   "message":"Email verification successful",
   "data":"Email verified successfully"
}

Resend Verification
URL: POST /api/auth/resend-verification
Body:{
    "email": "user@example.com",
    "password": "examplepassword"
}
Response:{
    "status": "success",
    "message": "Resend mail verification successful",
    "data": "Mail verification sent successfully"
}

Login
URL: POST /api/auth/login
Body:{
"email": "user@example.com",
"password": "examplepassword"
}
Response: {
    "status": "sucess",
    "message": "user@example.com is logged in",
    "data": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwicm9sZXMiOlsiVVNFUiJdLCJpZCI6MzUyLCJpYXQiOjE3NTQ5ODU0MjEsImV4cCI6MTc1NTA3MTgyMX0.kbXbTzKL0Z9HRQx2fMJZhQRLpnq_5oYYlwHhFb8w7ZY"
}

Logout
URL: POST /api/auth/logout
Header: Authorization: Bearer <token>
Response: {
    "status": "success",
    "message": "Logged out successfully",
    "data": null
}

User Management
(Admin role required)
Get all User
URL: GET /api/user
Header: Authorization: Bearer <token>
Response{
       "status": "success",
    "message": "Get all user successfully",
    "data": []
}

Get user by id
URL: GET /api/user/{id}
Header: Authorization: Bearer <token>
Response{
    "status": "success",
    "message": "Get user successfully",
    "data": {
        
    }
}

Create user
URL: POST /api/user
Header: Authorization: Bearer <token>
Body:{
    "email": "user2@example.com",
    "password": "example",
    "name": "TuyenLV5",
    "address": "HN",
    "phoneNumber": "0124331"
}
Response: {
    "status": "success",
    "message": "User created successfully",
    "data": {
        "id": 402,
        "name": "TuyenLV5",
        "email": "user2@example.com",
        "gender": "null",
        "phoneNumber": "0124331",
        "address": "HN",
        "avatar": null,
        "roles": [
            "USER"
        ],
        "active": false
    }
}

Update user
URL: PUT /api/user/{id}
Header: Authorization: Bearer <token>
Body:   {
        "name": "TuyenLV6",
        "email": "user2@example.com",
        "gender": "MALE",
        "phoneNumber": "0124331",
        "address": "HN",
        "active": false
}
Response:
{
    "id": 402,
    "name": "TuyenLV6",
    "email": "user2@example.com",
    "gender": "MALE",
    "phoneNumber": "0124331",
    "address": "HN",
    "avatar": null,
    "roles": [
        "USER"
    ],
    "active": false
}

Delete user
URL: DELETE /api/user/{id}
Header: Authorization: Bearer <token>
Response: User deleted successfully


Upload Avatar
URL: POST /api/user/{id}/avatar
Header: Authorization: Bearer <token>
Body: Form-data, key=file, value=image file (e.g., avatar.jpg)
Header: Authorization: Bearer <token>
Response: Path to uploaded avatar
Log: Check logs/info.log and logs/debug.log

Author Management

Create Author
URL: POST /api/author
Authorization: ADMIN role required
Body:{
    "name": "Author Name"
}
Response:{
    "status": "success",
    "message": "Author created successfully",
    "data": {
        "id": 1,
        "name": "Author Name"
    }
}

Get Author by ID

URL: GET /api/author/{id}
Authorization: No specific role (authenticated)
Response:{
    "status": "success",
    "message": "Author retrieved successfully",
    "data": {
        "id": 1,
        "name": "Author Name"
    }
}
Get All Authors

URL: GET /api/author
Authorization: No specific role (public or authenticated)
Response:{
    "status": "success",
    "message": "All authors retrieved successfully",
    "data": [
        {
            "id": 1,
            "name": "Author Name 1"
        },
        {
            "id": 2,
            "name": "Author Name 2"
        }
    ]
}
Update Author

URL: PUT /api/author/{id}
Authorization: ADMIN role required
Body:{
    "name": "Updated Author Name"
}
Response{
    "status": "success",
    "message": "Author deleted successfully",
    "data": "Author with ID 1 has been deleted"
}

Book Mangagement
/api/book

Create Book

URL: POST /api/book
Authorization: ADMIN role required
Body:{
    "title": "Book Title",
    "isbn": "978-3-16-148410-0",
    "publicationDate": "2023-10-15",
    "pageCount": 300,
    "quantity": 10,
    "categoriesId": [1, 2],
    "publisherId": 1,
    "authorIds": [1, 2],
    "libraryId": 1
}
Response:

Update Book

URL: PUT /api/book/{id}
Authorization: ADMIN role required
Body:{
    "title": "Updated Book Title",
    "isbn": "978-3-16-148410-0",
    "publicationDate": "2023-10-15",
    "pageCount": 300,
    "quantity": 10,
    "categoriesId": [1, 2],
    "publisherId": 1,
    "authorIds": [1, 2],
    "libraryId": 1
}
Response:

Delete Book

URL: DELETE /api/book/{id}
Authorization: ADMIN role required
Response:


Get Book by ID

URL: GET /api/book/{id}
Authorization: No specific role (public or authenticated)
Response:

Get All Books

URL: GET /api/book?page=0&size=10&sort=id,asc
Authorization: No specific role (authenticated)
Response:

Find Books by Author ID
URL: GET /api/book/author/id/{authorId}?page=0&size=10&sort=id,asc
Authorization: No specific role (public or authenticated)
Response:

Find Books by Author Name
URL: GET /api/book/author/name/{authorName}?page=0&size=10&sort=id,asc
Authorization: No specific role (public or authenticated)
Response:


Find Books by Category ID
URL: GET /api/book/category/id/{categoryId}?page=0&size=10&sort=id,asc
Authorization: No specific role (public or authenticated)
Response:

Find Books by Category Name
URL: GET /api/book/category/name/{categoryName}?page=0&size=10&sort=id,asc
Authorization: No specific role (public or authenticated)
Response:

Find Books by Publisher ID
URL: GET /api/book/publisher/id/{publisherId}?page=0&size=10&sort=id,asc
Authorization: No specific role (public or authenticated)
Response:

Find Books by Publisher Name

URL: GET /api/book/publisher/name/{publisherName}?page=0&size=10&sort=id,asc
Authorization: No specific role (public or authenticated)
Response:

Book Loan Management

Borrow Books
URL: POST /api/book-loan
Header: Authorization: Bearer <token>
Body:{
    "userId": 1,
    "bookIds": [1, 2]
}
Response: List of borrowed book loans

Return Book
URL: PUT /api/book-loan/return/{loanId}
Authorization: Authenticated user
Response:

Get Loans by User

URL: GET /api/book-loan/user/{userId}?page=0&size=10&sort=id,asc
Authorization: Authenticated user
Response:

Pay Fine

URL: PUT /api/book-loan/pay-fine/{loanId}
Authorization: Authenticated user
Response:

Extend Due Date

URL: PUT /api/book-loan/extend-dueDate/{loanId}
Authorization: Authenticated user
Body (optional){
    "dueDate": "2025-08-30",
    "days": 3
}

Categories: /api/categories/

Create Category

URL: POST /api/categories
Authorization: ADMIN role required
Body:{
    "name": "Category Name"
}
Get Category by ID

URL: GET /api/categories/{id}
Authorization: No specific role (public or authenticated)
Response:

Get All Categories

URL: GET /api/categories
Authorization: No specific role (public or authenticated)
Response:

Update Category

URL: PUT /api/categories/{id}
Authorization: ADMIN role required
Body:

Delete Category

URL: DELETE /api/categories/{id}
Authorization: ADMIN role required
Response:

Publishers: /api/publisher/

Create Publisher

URL: POST /api/publisher
Authorization: ADMIN role required
Body:{
    "name": "Publisher Name",
    "address": "Publisher Address"
}
Response:

Update Publisher

URL: PUT /api/publisher/{id}
Authorization: ADMIN role required
Body:{
    "name": "Updated Publisher Name",
    "address": "Updated Publisher Address"
}
Response:

Delete Publisher

URL: DELETE /api/publisher/{id}
Authorization: ADMIN role required
Response:

Get Publisher by ID

URL: GET /api/publisher/{id}
Authorization: No specific role (public or authenticated)
Response:

Get All Publishers

URL: GET /api/publisher
Authorization: No specific role (public or authenticated)
Response:

Libraries: /api/library/

Create Library

URL: POST /api/library
Authorization: ADMIN role required
Body:

Get Library by ID

URL: GET /api/library/{id}
Authorization: No specific role (public or authenticated)
Response:

Get All Libraries

URL: GET /api/library
Authorization: No specific role (public or authenticated)
Response:

Update Library

URL: PUT /api/library/{id}
Authorization: ADMIN role required
Body:{
    "name": "Updated Library Name",
    "location": "Updated Library Location"
}
Response

Delete Library

URL: DELETE /api/library/{id}
Authorization: ADMIN role required
Response:


6. Logging
   Logs are split into separate files for better management:
logs/info.log: General application logs (INFO level)
logs/debug.log: Detailed debugging logs (DEBUG level)
logs/error.log: Error logs (ERROR level)
7. File Uploads

Avatar Storage: Images are stored in ./Uploads/images/ (dev) or /var/libman/uploads/images/ (prod).
Max File Size: 5MB (configurable via SPRING_SERVLET_MULTIPART_MAX_FILE_SIZE).

8. Security

JWT: Tokens include userId, email, and roles. Only authorized users (matching userId or ADMIN) can perform sensitive actions.
Environment Variables: Sensitive data (e.g., SPRING_DATASOURCE_PASSWORD, SPRING_MAIL_PASSWORD, JWT_SECRET) should be set via environment variables or profile-specific properties.

9. Future Improvements

Cloud Storage: Integrate AWS S3 for avatar storage.
PDF Invoices: Use iText to generate PDF,excel invoices.
Audit Logging: Add a table to track user actions (e.g., avatar uploads, invoice generation).
Docker: Containerize the application for easier deployment.
Rate Limiting: Implement rate limiting for API endpoints.
