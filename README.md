# University database lab - Spring Boot

Spring Boot 4.1.2 / Java 21 implementation of Lab Sheet 06, Tasks 8-12.
The application reads your existing `university_db` database. It never runs the SQL files,
creates tables, or inserts sample data. Hibernate uses `ddl-auto=validate`.

## Start

Start MySQL first. In a terminal in this project, set your credentials and start the app:

```bash
export DB_USERNAME=malan
read -rsp 'MySQL password: ' DB_PASSWORD; echo
export DB_PASSWORD
./mvnw spring-boot:run
```

The password is entered invisibly and is not saved in the repository.
`DB_URL` can override the default connection to `localhost:3306/university_db`.
The API is at `http://localhost:8080/api`. Stop it with Ctrl+C.

## Postman

1. Import `postman/university-db.postman_collection.json` into Postman.
2. The collection variable `baseUrl` defaults to `http://localhost:8080/api`.
3. Use **Run collection** to execute all 17 GET requests and their response assertions.
4. View the test results for each response. Expected data matches `sql/data.sql`.
   Changed database contents can cause the sample-data assertions to fail.

All requests below use GET, have no body, and require no authentication.

| Path (after `/api`) | Expected result with lab sample data |
| --- | --- |
| `/students` | 4 students sorted by name; Dilini first |
| `/students?name=sil` | Kamal Silva |
| `/students/S001` | Kamal's code, full name and email |
| `/students/S001/enrollments` | CS101 (A), CS102 (B+) |
| `/courses` | 4 courses, with lecturer and department |
| `/courses?minCredits=4` | CS201 only |
| `/courses?department=Computer%20Science` | CS101, CS102, CS201 |
| `/courses/CS101/students` | Kamal and Nimali |
| `/departments/D01/lecturers` | Dr. Perera and Dr. Fernando |
| `/enrollments?grade=A` | S001/CS101 and S004/CS201 |
| `/reports/course-counts` | CS101:2, CS102:2, ST201:2, CS201:1 in that order |
| `/enrollments?grade=B%2B` | S001/CS102; encode `+` as `%2B` |
| `/students/UNKNOWN` | 404 |
| `/departments/UNKNOWN/lecturers` | 404 |
| `/enrollments` | 400: required grade missing |
| `/courses?minCredits=abc` | 400: invalid integer |
| `/students?name=NoSuchStudent` | 200 with `[]` |

As in the guide, `minCredits` takes priority if both course filters are supplied.
Unknown student/course codes on collection endpoints return `[]`.

Example response for `/api/courses?minCredits=4`:

```json
[
  {
    "code": "CS201",
    "title": "Web Development",
    "credits": 4,
    "lecturer": "Dr. Perera",
    "department": "Computer Science"
  }
]
```

You can run the same collection assertions from a terminal with Node 18+:

```bash
node postman/check-requests.mjs
```

## Automated tests

```bash
./mvnw test
```

Tests use a separate in-memory H2 database in MySQL compatibility mode. The test-only
schema and data are copies of the lab SQL adjusted for H2. Hibernate validates those
tables before tests run. Tests cover every custom repository method, report ordering,
HTTP serialization of relationships, and 400/404 cases. They do not connect to MySQL.
The Postman checks against the running application verify the actual MySQL integration.

## Code layout

- `model`: five entities mapped to the existing columns, foreign keys and enrollment uniqueness.
- `repository`: all five repositories and all custom queries from Task 10.
- `dto`: four entity DTO records, report projection and report DTO.
- `controller/UniversityController`: eight read-only endpoint mappings with query parameters.

DTO conversion occurs inside a read-only transaction so the department's lecturer
collection remains available with `open-in-view=false`. The controller exposes no writes.

## Guide experiments and screenshots

The Task 9 schema-change experiments are manual exercises; they are not run on your database
by this implementation. For the missing-column screenshot, temporarily add the `phone`
field shown in the guide to `Student`, start with `validate`, and capture the startup failure.
If you perform the guide's `update` experiment, restore the entity, remove the extra column,
and return to `validate` afterward. Do not use `create` or `create-drop` on your MySQL data.

For Task 12 screenshots, capture Postman responses and the SQL printed in the application
terminal. The department query uses `LEFT JOIN FETCH`; the report uses JPQL `GROUP BY`.
The course department filter traverses course -> lecturer -> department. Default eager
many-to-one mappings can produce additional selects, as discussed in the guide.
