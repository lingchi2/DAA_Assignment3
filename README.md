# DAA_Assignment_3
## Build and test
```bash
mvn clean test
```

## Run benchmark
```bash
mvn -q -DskipTests package
java -cp target/classes org.example.benchmark.Benchmark
```