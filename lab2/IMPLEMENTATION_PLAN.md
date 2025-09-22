# Lab 2 Implementation Plan - Discrete Event Simulation System

## Current Status (Session Checkpoint)
Date: 2025-09-22

### Completed Tasks:
- ✅ **Task 1 (5 points)**: Simple single-device service model using OOP approach - ALREADY IMPLEMENTED
  - Base Element class with time management
  - Create class for entity generation
  - Process class for single-device service
  - Model class for simulation control
  - Event-driven simulation using "next event" principle

### Remaining Tasks:

**Task 2 (5 points): Add Average Device Utilization**
- Modify Process class to track device busy time
- Calculate utilization = busy_time / total_simulation_time
- Add to statistics output

**Task 3 (30 points): Create Multi-Process Model (Figure 2.1)**
- Implement Dispose class for entity disposal
- Build chain: CREATE → PROCESS1 → PROCESS2 → PROCESS3 → DISPOSE
- Configure different processing times for each process
- Test the complete workflow

**Task 4 (10 points): Model Verification**
- Create verification scenarios with different parameters
- Test: arrival rates, service times, queue sizes
- Generate results table comparing theoretical vs simulated values
- Validate queue theory formulas (M/M/1 system)

**Task 5 (20 points): Multi-Device Process Support**
- Extend Process class with deviceCount parameter
- Implement parallel service channels
- Track individual device states and utilization
- Handle queue management for multiple servers

**Task 6 (30 points): Advanced Routing System**
- Add routing probability support to Element class
- Support multiple nextElement destinations
- Implement backward routing (loops in the model)
- Add conditional routing based on entity properties

## Current Codebase Structure:

### Files Present:
- `/src/main/java/org/example/SimModel.java` - Main class
- `/src/main/java/org/example/Model.java` - Simulation controller
- `/src/main/java/org/example/Element.java` - Base element class
- `/src/main/java/org/example/Process.java` - Service process element
- `/src/main/java/org/example/Create.java` - Entity generator
- `/src/main/java/org/example/simsimple/FunRand.java` - Random number generators

### Key Features Already Implemented:
- Event-driven simulation with next event principle
- Queue management with configurable limits
- Statistics collection (quantity, mean queue length, failure probability)
- Multiple probability distributions (exponential, normal, uniform)
- Basic OOP structure with inheritance

## Next Steps for Continuation:
1. Start with Task 2 (device utilization) - simple modification to existing Process class
2. Move to Task 3 (multi-process chain) - create Dispose class and chain setup
3. Continue with remaining tasks in order of complexity

## Technical Notes:
- Framework uses discrete event simulation principles from the PDF
- Time advancement via "next event" method
- Statistics collected during simulation runtime
- All random distributions available via FunRand class
- Model verification should compare with M/M/1 queue theory formulas

---
*Session paused here - ready to continue implementation*