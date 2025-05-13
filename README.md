# Skill Tree Creator

Skill Tree Creator is a Java-based desktop application that allows users to create, visualize, and export skill trees. The application provides functionality to convert skill tree data from JSON files to PDF format and preview the skill tree as a diagram using Mermaid.js.

## Features
- **Add Files**: Import JSON files containing skill tree data.
- **Convert to JSON**: Process and save skill tree data in JSON format.
- **Convert to PDF**: Export skill tree data to a PDF file.
- **Preview Skill Tree**: Visualize the skill tree as a diagram using Mermaid.js.
- **Multi-File Support**: Add and process multiple files at once.

## Technologies Used
- **Java Swing**: For the graphical user interface.
- **JavaFX**: For rendering the Mermaid.js diagrams.
- **Gson**: For JSON parsing and serialization.
- **iText**: For generating PDF files.
- **Mermaid.js**: For creating skill tree diagrams.

## Prerequisites
- **Java 17 or higher**: Ensure you have Java Development Kit (JDK) installed.
- **Maven**: For managing dependencies and building the project.

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/skill-tree-creator.git
   cd skill-tree-creator
   ```
2. Build the project using Maven:  
  ```bash
  mvn clean install
```
3.Run the application:  
  ```bash
  java -jar target/skill-tree-creator.jar
  ```

## Usage
1. Launch the application.
2. Use the Add File button to import JSON files containing skill tree data.
3. Select files from the input list and:
    - Click Convert to JSON to process and save the data in JSON format.
    - Click Convert to PDF to export the data to a PDF file.
4. Use the Preview button to visualize the skill tree as a diagram.

## File Structure
- src/MyFrame.java: Main application frame and UI logic.
- src/MermaidRender.java: Handles rendering of skill tree diagrams using Mermaid.js.
- src/PDF_Parser.java: Converts skill tree data from JSON to PDF format.
- src/JSON_Parser.java: Parses and processes JSON files.
- src/MyFrame.form: UI layout for the main frame.

## Known Issues
- Ensure JavaFX is properly configured in your environment to render diagrams.
- Mermaid.js requires an active internet connection to load its library.
- **Current Issue:** The application does not yet support exporting the diagram preview as a PNG image.

## Acknowledgments
- Mermaid.js for diagram rendering.
    - https://mermaid.js.org/#/
- iText for PDF generation.
    - https://itextpdf.com
- Gson for JSON parsing
    - https://github.com/google/gson
