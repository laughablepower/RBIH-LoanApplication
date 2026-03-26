RBIH-LoanApplication

## Overall Approach

This service follows a clean layered architecture with a clear separation between web (controller), orchestration (service), domain logic (pure static utility classes), and persistence (JPA repository). The domain logic classes (EmiCalculator, RiskClassifier, InterestRateCalculator, EligibilityEvaluator) are intentionally static and stateless, making them trivially unit-testable.

## Key Design Decisions

**Records for DTOs**: Java records provide immutable, concise request/response objects with built-in equals, hashCode, and toString. Lombok is not needed for DTOs since records handle this natively.

**Lombok for JPA entity**: The LoanApplicationEntity uses @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, and @Builder.

**Static utility classes for domain logic**: EMI calculation, risk classification, interest rate computation, and eligibility evaluation are pure functions. They take inputs, return outputs with no need to instantiate. This makes them easy to reason about, test in isolation, and reuse.

**Used enums**: The application has used enum values to avoid magic values.Handled via ExceptionHandler in case unknown value arrives from input.

**BigDecimal throughout**: All financial calculations use BigDecimal with scale 2 and RoundingMode.HALF_UP as specified. No floating-point error.

**Single entity for audit**: One LoanApplicationEntity captures the entire application lifecycle - applicant data, loan params, decision, offer details, and rejection reasons. Rejection reasons are stored as a comma-separated string for simplicity

## Trade-offs Considered

- **Rejection reasons as CSV string vs. separate table**: A normalised rejection_reasons table would be cleaner for querying, but adds complexity disproportionate to the scope.
- **Static utility classes vs. Spring @Component**: Spring beans would allow future AOP (logging, metrics). Given the scope, static methods are simpler.
- **H2 in-memory vs. persistent DB**: H2 keeps the project self-contained. For production, swap the datasource config to PostgreSQL.
- **No authentication Layer**: since in early development phase, avoided authentication 

## Assumptions

- The eligibility EMI check seems to have ambiguity between conditions(Whether to choose between 50% OR 60% for Validation). Hence, have used EMI> 50 % of salary.
- The age + tenure boundary (> 65) is exclusive - exactly 65 is allowed.
- Both APPROVED and REJECTED responses return HTTP 200. The 400 status is reserved for validation failures only.

## Improvements With More Time

- Add integration tests using @SpringBootTest and MockMvc.
- Add Swagger/OpenAPI documentation via springdoc-openapi.
- Add a GET /applications/{name} endpoint for retrieving past decisions.
- Extract domain services into Spring beans to enable AOP-based audit logging.
- Add structured logging with correlation IDs.
- Add persistence via PostgreSQL.