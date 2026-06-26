

# The Closet 🛍️✨

A full-stack e-commerce clothing store built with Java, Spring Boot, MySQL, and a custom frontend. This application allows users to browse products, create accounts, add items to their shopping cart, and place orders through a secure checkout process.

## About the Project

For my Capstone project, I chose to create a clothing store because I wanted to build something that felt realistic and fun to work on. Throughout this project, I implemented REST APIs, authentication, shopping cart functionality, user profiles, and order processing.

This project challenged me to think about how different pieces of an application work together behind the scenes and gave me a much deeper understanding of backend development.

---

## Features

- User Registration and Login with JWT Authentication
- Browse Products and Categories
- Search and Filter Products
- Shopping Cart Functionality
- Add, Update, and Remove Cart Items
- User Profile Management
- Checkout and Order Creation
- RESTful API Design
- API Testing with Insomnia and Swagger

---

## Technologies Used

- Java
- Spring Boot
- Spring Security
- MySQL
- Maven
- JWT Authentication
- Insomnia
- Swagger
- HTML
- CSS
- JavaScript

---

## Biggest Challenge

The hardest part of this project was figuring out the relationship between the **Shopping Cart**, **Orders**, and **Order Line Items**.

At first, all of these classes felt like they were doing the same thing. I had to really slow down and understand that:

- The **Shopping Cart** is temporary and stores what the user wants to buy.
- The **Order** is the final purchase that gets created during checkout.
- The **Order Line Items** are the individual products that belong to that order.

Once I understood how all of these moving pieces connected together, the checkout process finally started making sense. It was one of those moments where everything clicked, and I felt like I had leveled up as a developer.

---

## Favorite Code

My favorite code block in this project was inside the **Order Controller** because it tied together everything I had been working on.

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public Order checkout(Principal principal) {
    String username = principal.getName();
    User user = userService.getByUserName(username);

    return orderService.checkout(user.getId());
}