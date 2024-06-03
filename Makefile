runApp: App.class
	java App

App.class: App.java Song.class SongInterface.class SortedCollectionInterface.class FrontendInterface.class Frontend.class Backend.class
	javac -cp .:../junit5.jar App.java

runBDTests: Song.class SongInterface.class BackendInterface.class ISCPlaceholder.class SortedCollectionInterface.class FrontendInterface.class FrontendPlaceholder.class BackendPlaceholder.class Backend.class
	javac -cp .:../junit5.jar BackendDeveloperTests.java
	java -jar ../junit5.jar -cp . -c BackendDeveloperTests

Song.class: Song.java
	javac Song.java

SongInterface.class: SongInterface.java
	javac SongInterface.java

ISCPlaceholder.class: ISCPlaceholder.java
	javac ISCPlaceholder.java

FrontendPlaceholder.class: FrontendPlaceholder.java
	javac FrontendPlaceholder.java

SortedCollectionInterface.class: SortedCollectionInterface.java
	javac SortedCollectionInterface.java

BackendPlaceholder.class: BackendPlaceholder.java
	javac BackendPlaceholder.java

Backend.class: Backend.java
	javac Backend.java

clean:
	rm *.class

runFDTests: FrontendDeveloperTests.class Frontend.class
	java -jar ../junit5.jar -cp . -c FrontendDeveloperTests

FrontendDeveloperTests.class: FrontendDeveloperTests.java TextUITester.class
	javac -cp ../junit5.jar: FrontendDeveloperTests.java

TextUITester.class: TextUITester.java
	javac TextUITester.java

Frontend.class: Frontend.java FrontendInterface.class BackendInterface.class BackendPlaceholder.class
	javac Frontend.java

FrontendInterface.class: FrontendInterface.java
	javac FrontendInterface.java

BackendInterface.class: BackendInterface.java
	javac BackendInterface.java

BackendPlaceHolder.class: BackendPlaceholder.java
	javac BackendPlaceholder.java
