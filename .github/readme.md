# <samp>Hello!, thank you for watching this little project.</samp> :hot_face:

<b>Deadly Speed</b>

This is a game of confrontation and strategy because when attacking you must also think if you can escape.

I have tried to make the code understandable so that you can build more game modes. 

So I challenge you to build a new mode with a new game logic. Just make sure your classes comply with the contract of the created interfaces. :hammer_and_pick:

## :eyes: <samp>Two game modes</samp>

| <b>Quick Game</b>                                                                                     |
|-------------------------------------------------------------------------------------------------------|
| <a href="#--------"><img src="assets/quickgame.gif" width="700px" alt="quick game"></a>               |


| <b>Champions League Game</b>                                                                          |
|-------------------------------------------------------------------------------------------------------|
| <a href="#--------"><img src="assets/leaguegame.gif" width="700px" alt="Champions league game"></a>   |


## :wrench: <samp>Test the game</samp>

:warning: The game is not on the internet but you can try it on your own operating system. With theses setups are will enable the Quick Game.

<b>1. Install Required Dependencies</b>

Assuming that your OS is Arch Linux :skull:

> Install [Maven](https://maven.apache.org/)


```sh
sudo pacman -S mariadb maven
```

:warning: Install MariaDB if you want to play Champions League. After configuring mariadb, create a database with the following date [`src/main/resources/confidential/database.properties`](https://github.com/jorghee/Deadly-Speed/blob/main/src/main/resources/confidential/database.properties)

Finally, create the following tables [`src/main/resources/sql/tables.sql`](https://github.com/jorghee/Deadly-Speed/blob/main/src/main/resources/sql/tables.sql)

<b>2. Install Deadly Speed</b>

> Clone this repository

```sh
git clone https://github.com/jorghee/Deadly-Speed.git
cd Deadly-Speed
```

> Use Maven to manager the dependencies

:warning: You must modify the [`pom.xml`](https://github.com/jorghee/Deadly-Speed/blob/main/pom.xml) file to compile the project with the specific or minor version installed on your operating system. 

```xml
<maven.compiler.source>17</maven.compiler.source>
<maven.compiler.target>17</maven.compiler.target>
```
The minimun version required to this project is java 17

> Compile and run with Maven's javafx-maven-plugin plugin
```sh
mvn javafx:run
```


