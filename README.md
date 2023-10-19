## 👋 Demo Gatling Performance Tests
This is a Demo Performance Testing Challenge, developed by Douglas Urrea Ocampo using Gatling to improve learning on Performance testing.

## 🏠 Tester details
* Name: `Douglas Urrea Ocampo`
* Country: `Colombia`
* City: `Medellin`
* E-mail: `itmteleco@gmail.com`
* LinkedIn: [https://www.linkedin.com/in/douglasfugazi](https://www.linkedin.com/in/douglasfugazi)
* Contact: [https://douglasfugazi.co](https://douglasfugazi.co)

## ✨ Pre-requisites:
1. Install Gatling from [here](https://gatling.io)
2. Install JAVA SDK from [here](https://aws.amazon.com/es/corretto/)
3. Install git from [here](https://git-scm.com)
4. Install Apache Maven from [here](https://maven.apache.org)

## 🛠️ Running the project:
1. Download the project from GitHub
    * Option 1: `git clone https://github.com/fugazi/Gatling-Demo-Performance-Tests.git`
    * Option 2: Download it as a Zip file and extract it
2. CD into the `demo-gatling-perf-tests` folder
3. Set up Apache Maven
    * Maven: `Update Maven Archetype Catalog`
4. Set up Gatling Project
    * Maven: Create Maven Project Type adding `Gatling` as archetype
5. Run `mvn clean install` in the path of the project
6. Open a terminal or command prompt to Run Gatling test locally
    * Run `mvn gatling:test`

### ✏️ Step by Step tutorial

TBA

### 🚴 Project configuration
* Gatling project Baseline: `demo-gatling-perf-tests`
* Gatling simulations `AirportGapPerfTest.java` (with Bearer Token) into folder `src\test\java\simulations`
* Gatling simulations `BookingPerfTest.java` (with Basic Auth) into folder `src\test\java\simulations`
* Gatling resources into folder `src\test\resources`
* Test folder: `simulations`
