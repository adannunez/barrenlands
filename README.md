# Barren Land Analysis
A fun technical assessment case study. Please refer to the original case study definition file for the specifics about the case study. It is included in this repo as well.

## Running the Application
Bellow are two main ways to run the application. Once it's running, it should accept input and produce output in the console/terminal as defined in the case study.

### IntelliJ IDEA
If you have IntelliJ IDEA set up you should be able to get this running like any other basic Gradle project.
1. Extract the `.zip`
1. Import the extracted project into IntelliJ
1. Refresh Gradle
1. Run/Debug the `main` method in `BarrenlandApplication`

### Gradle Commands
The other way to run the application is through Gradle commands.
1. Extract the `.zip`
1. Navigate to the root directory of the extracted project
1. Execute the following Gradle command to clean, build, test, and run the application:

    ```shell script
   ./gradlew clean build run --console plain
    ```

## General Approach
1. Initialize a farmland grid based on farm size and the given barren areas.
1. Perform DFS to traverse that grid keeping track fertile surface areas.

## General Reasoning
The facts that "the remaining area of fertile land is in no particular shape" and that we ultimately need the "fertile land area in square meters" immediately hinted that some kind of graph traversal within the farmland grid would be necessary.

Since counting the surface area requires visiting every cell, there is no significant benefit to Depth First Search or Breadth First Search. A tail recursive implementation of DFS was chosen to take advantage of the compiler optimizations provided by using the `tailrec` modifier.

## Runtime Complexity
Runtime complexity could get unwieldy but the following restrictions help control it:
1. The number of barren land areas can be at most 1000.
    - This impacts the runtime of initializing the farm land data structure and marking the appropriate areas as barren.
1. Every cell can have at most 4 neighbors.
    - This impacts the runtime of the DFS.
    
Given those restrictions, there is a constant upper bound on the number of times each cell will be visited either during initialization or traversal. So our runtime complexity is `O(V)` where `V` is the number of cells.

## Extra Notes
1. A few more things could be parameterized such as farm size and max number of barren areas.
1. Making it "production ready" could go in many directions. For example:
    1. If we don't expect functionality to get much more complex, this could be exposed to users as a lambda in AWS. Pretty much everything is static, there's no state, no data persistence... might need to pick something with a better cold start though.
    1. If we want to turn this into a "Farm Management Service" with Farmer accounts and persisted farm information, this can become a more robust service with data persistence, security, user management, etc.
1. I hadn't written such a simple JVM application without Spring Boot in a while... it was a fun rabbit whole figuring out how to make the STDIN/STDOUT stuff more testable.
1. Error handling is consciously minimal. Something more standard and robust might be necessary depending on the intended audience and use cases.