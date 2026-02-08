# Wallet Service Backend

## Overview

This project implements a wallet service for a closed-loop virtual credit system (gaming/loyalty style).
It supports wallet top-ups, spending, bonus credits, idempotent transactions, concurrency safety, and a ledger-based architecture.

---

## Tech Stack & Why

**Spring Boot (Java 17)**
Reliable backend framework with strong transactional support.

**SQLite**
Lightweight file-based DB — easy setup for demo/assignment use.

**Spring Data JPA / Hibernate**
Simplifies DB interaction and supports transactional consistency.

---

## Database Setup & Seed Data

Seed data is provided in:

```
src/main/resources/data.sql
```

It initializes:

* Asset types
* Users
* System wallet
* Initial balances

### Run Locally

```
mvn clean package
java -jar target/wallet-service-0.0.1-SNAPSHOT.jar
```

SQLite DB (`wallet.db`) is auto-created.

---

## Concurrency Strategy

* **Transactional operations** ensure atomic updates.
* **Pessimistic row locking** prevents concurrent balance corruption.
* **Deadlock avoidance** via consistent lock ordering + retry logic.
* **Idempotency** enforced using unique transaction reference IDs.

---

## Ledger Architecture

Double-entry ledger model:

* Debit and credit entries recorded for every transaction.
* Balance reconciled with ledger for auditability.

---

## Main APIs

```
GET  /wallet/{walletId}                       -> Check balance
POST /wallet/{walletId}/topup                 ->  Add credits
POST /wallet/{walletId}/spend                 -> Spend credits
GET  /wallet/{walletId}/transactions          -> Transaction history
POST /wallet/{walletId}/bonus?amount=X&ref=ID -> Bonus

```
