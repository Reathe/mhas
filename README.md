# MHAS: Metaheuristics for the Travelling Salesman Problem

> **Frankenstein** is a hybrid metaheuristic in Java for the Travelling Salesman Problem (TSP). It combines a
> genetic algorithm, ant colony optimization and hill climbing, and picks the most useful mutation operator
> while it runs.

![Java](https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=white)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

Built by **Rafael Bachourian** and **Enes Usta** for a university metaheuristics course, where solvers were compared
on the same TSP instances under a fixed time limit. The full write-up (in French)
is in [`Frankenstein.pdf`](Frankenstein.pdf).

## The algorithm

Frankenstein is a **genetic algorithm** with a population of 200 tours, reinforced by several other techniques.
The name comes from the way it is stitched together from these parts.

| Component | Role |
| --- | --- |
| **Smart initialization** | 50 individuals come from an **ant colony optimization** run, 50 from the **nearest-neighbour** heuristic (random start city), and the rest are random tours, for a mix of quality and diversity |
| **Crossover** | A modified order crossover (`CrossoverMOC`, single pivot; a `CrossoverPMX` partially-mapped crossover is also implemented) builds the worst quarter of the population from parents chosen in the best half |
| **Mutation pool** | Random swap, neighbour swap, random shift, and reversal of a random segment (2-opt-like) |
| **Adaptive operator selection** | Each mutation has a score that grows with the improvement it produces (`log(Δ + 1)`) and slowly decays. Operators are drawn with probability proportional to their score (roulette wheel), so the search keeps using what works on the current instance |
| **Intensification** | When the best tour hasn't improved for a while, **hill climbing** (20,000 steps) is run on it to reach a local optimum |
| **Elitism** | The population is sorted every generation, so the best half survives unchanged |

A standalone **2-opt** solver (`tsp.projects.opt2`) is included as a baseline.

## Getting started

### Requirements

- JDK 8+

### Build and run

```bash
git clone https://github.com/Reathe/mhas
cd mhas

# Compile (dependencies are in lib/)
javac -cp "lib/*" -d out $(find src -name "*.java")

# Run every solver on every instance in data/
java -cp "out:lib/*" tsp.run.Main          # on Windows, use "out;lib/*"
```

The runner (`tsp.run.Main`, provided by the course) uses reflection to find every concrete `Project`
subclass in `tsp.projects`. It runs each one on every `.tsp` file in `data/` for **30 seconds**, then prints and logs the best
tour length found (`tsp.log`).

### Benchmark instances

| File | Cities |
| --- | --- |
| `data/bier127.tsp` | 127 |
| `data/gr666.tsp` | 666 |
| `data/fnl4461.tsp` | 4,461 |

To add an instance, drop a file in the same format into `data/`.

## Project structure

```
src/tsp/
├── evaluation/          # Problem loading, Path representation, tour evaluation (course framework)
├── output/              # Console / log-file output (course framework)
├── run/Main.java        # Benchmark runner with time budget (course framework)
└── projects/
    ├── frankenstein/    # ← our solver
    │   ├── Frankenstein.java   # GA main loop, adaptive mutation selection
    │   ├── ants/               # Ant colony optimization (population seeding)
    │   ├── crossover/          # MOC & PMX crossovers
    │   ├── mutation/           # Mutation operators
    │   └── recuit/             # Hill climbing
    └── opt2/            # 2-opt baseline
```

## License

[MIT](LICENSE.md)
