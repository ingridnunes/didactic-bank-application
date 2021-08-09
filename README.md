# didactic-bank-application

A simple banking system written in Java used to teach object-oriented programming and best coding practices. It is a three-layered application, with: (i) a UI layer, with a command line interface and a graphical interface (in Java Swing); (ii) a business layer; and (iii) a data layer, implemented in memory. 

The project contains the required libs so there is no need for students that are learnign object orientation to use any additional tool. The project is configured in Eclipse.

## Features

* **Login**
  * Branch: employees provide their user and password
  * ATM: clients provide their account number, branch number, and password
* **Logout**
* **Create Account** (only in branches)
  * New banck account associated with a branch and a client is created
  * A certain value informed as initial balance
* **Check Balance**
  * System shows the account balance and current date and time
* **Statement**
  * System shows all month operations in the month or selected period
    * Last month (one of the last 6 months)
    * Period (if no data is provided, last 30 days)
* **Deposit**
  * Client provides the desired value and informs the number of the envolope to give the money
* **Withdrawal**
  * Client provides the desired value
* **Transfer**
  * Client provide the branch, target account, and the desired value

## Packages

* `bank`: application initialisation
* `bank.business`: application services (interfaces)
* `bank.business.domain`: domain classes
* `bank.business.impl`: application services (implementation)
* `bank.data`: application database (in memory)
* `bank.resources`: text messages
* `bank.ui`: user interface (top-level package)
* `bank.ui.graphic`: graphical user interface
* `bank.ui.graphic.action`: graphical user interface (actions)
* `bank.ui.text`: textual user interface
* `bank.ui.text.command`: textual user interface (commands)
* `bank.util`: util classes

# Running the Application

Execute the `bank.Bank` class, which contains the `main` method. It starts the application with the graphical user interface. To execute the application with the textual user interface, start the application with `-t`.

# Additional Documentation

* Check the docs folder, which contains UML class diagrams and a sequence diagram (Withdrawal feature).
* YouTube videos (in Portuguese)
  * [Modulo 3b - Sistema Bancario 1](https://youtu.be/FG50K3VsF4Q) - introduction to the bank application
  * [Modulo 3b - Sistema Bancario 2](https://youtu.be/wqgIMFyc9RI) - demonstration of how to run the bank application

*Observation*: This application has been used within the *INF01120 - Programming Construction Techniques* course at the Informatics Institute of UFRGS. Students are required to understand and evolve its code. Classes taught before students are given this assignment: object-oriented programming, basic UML (class and sequence diagrams), and code coventions and best practices. The students already know (from previous courses) procedural programming (in C) and data structures.
