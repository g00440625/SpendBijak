# SpendBijak — AI-Powered Financial Advisor API

> "Can I afford this?" — SpendBijak answers with a weighted risk score, not just a yes or no.

[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen)](https://spring.io/projects/spring-boot)
[![AWS EC2](https://img.shields.io/badge/AWS-EC2-yellow)](https://aws.amazon.com/ec2)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)](https://www.postgresql.org)

🔗 **Live API:** https://api.heleneathalia.ie  
📱 **Frontend:** https://spend-bijak-frontend.vercel.app  
👩‍💻 **Author:** Helene Athalia — BSc Software Development, ATU Galway

---

## The Problem It Solves

Most budgeting apps just track spending. SpendBijak tells you *before* you spend — using a real financial risk algorithm — whether a purchase is safe, risky, or unaffordable given your current situation.

---

## How The Decision Engine Works

When you ask "Should I buy this €900 laptop?", the engine calculates:

| Factor | Weight | What It Checks |
|---|---|---|
| Emergency Fund Ratio | 30% | Do you have 3-6 months of expenses saved? |
| Affordability | 25% | Is this purchase >50% of remaining income? |
| Savings Rate | 20% | What % of your income are you saving monthly? |
| Goal Impact | 10% | Does this delay your savings goals? |

Scores are **normalised against active weights** — so verdicts stay accurate even with partial data.

**Example response:**
```json
{
  "verdict": "MODERATE",
  "riskScore": 48.5,
  "remainingAfter": 1100.0,
  "reason": "Your emergency fund covers 1.6 months (recommended: 3-6). This purchase is 36% of your monthly income. Risk score: 49/100."
}
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3.5 |
| Auth | Spring Security + JWT |
| Database | PostgreSQL |
| Testing | JUnit 5 + Mockito |
| Build | Maven |
| Cloud | AWS EC2 (Ubuntu 24.04) |
| Proxy | nginx + Let's Encrypt SSL |

---

## Project Structure

src/main/java/com/helene/spendbijak/
├── config/ # JWT, Security, CORS
├── controller/ # REST endpoints
├── model/
│ ├── dto/ # Request/Response objects
│ └── entity/ # Database entities
├── repository/ # Data access layer
└── service/ # Business logic + decision engine


---

## API Reference
Public

POST /api/auth/register
POST /api/auth/login

Protected (Bearer token required)

GET /api/summary/user/{id}
POST /api/decision/user/{id}
POST /api/expenses
GET /api/expenses/user/{id}
POST /api/budgets
POST /api/goals
PUT /api/goals/{id}/progress


---

## Run Locally

**Prerequisites:** Java 21, PostgreSQL, Maven

```bash
git clone https://github.com/g00440625/spendbijak.git
cd spendbijak
cp src/main/resources/application.properties.example \
   src/main/resources/application.properties
# fill in your DB password and JWT secret
./mvnw spring-boot:run
```

API available at `http://localhost:8080`

---
