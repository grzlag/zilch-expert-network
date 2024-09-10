# Infrastructure
- better local scripting for local env startup and developing
- setup deployment with CI/CD
- in docker-compose locally spring boot apps do not work and flagd have problem (connectivity)

# Code
- Rewrite [PaymentService.java](../../services/core/src/main/java/com/zilch/zen/core/payments/PaymentService.java)
- Reduce amount of classes in [utils](../../services/core/src/main/java/com/zilch/zen/core/utils)
- Rewrite and reduce amount of code in integration tests for [PurchaseCreationTest.java](../../services/core/src/test/java/com/zilch/zen/core/purchase/PurchaseCreationTest.java) and [PaymentCreationTest.java](../../services/core/src/test/java/com/zilch/zen/core/payments/PaymentCreationTest.java)
- Add logger and logging
- Add tests in message sender service... 