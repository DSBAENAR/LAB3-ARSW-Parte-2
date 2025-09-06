## Escuela Colombiana de Ingeniería

## Arquitecturas de Software

# Componentes y conectores - Parte I

El ejercicio se debe traer terminado para el siguiente laboratorio (Parte II).

#### Middleware- gestión de planos

## Antes de hacer este ejercicio, realice [el ejercicio introductorio al manejo de Spring y la configuración basada en anotaciones](https://github.com/ARSW-ECI/Spring_LightweightCont_Annotation-DI_Example)

En este ejercicio se va a construír un modelo de clases para la capa lógica de una aplicación que permita gestionar planos arquitectónicos de una prestigiosa compañia de diseño.

![](img/ClassDiagram1.png)

1. Configure la aplicación para que funcione bajo un esquema de inyección de dependencias, tal como se muestra en el diagrama anterior.

 Lo anterior requiere:

* Agregar las dependencias de Spring. <br>
 Para este proyecto hice una actualización pasando las dependencias a SpringBoot

 ```pom
  <?xml version="1.0" encoding="UTF-8"?>
 <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
     <modelVersion>4.0.0</modelVersion>

    <groupId>edu.eci.pdsw.examples</groupId>
    <artifactId>blueprints-middleware</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>Blueprints_Middleware</name>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.3</version>
        <relativePath/>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>17</java.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.13.4</version>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

 </project>

  ```

* Agregar la configuración de Spring.

 Y creé el método main dentro de la clase creada BlueprintApplication

 ```java
  package edu.eci.arsw.blueprints;
 
 import org.springframework.boot.SpringApplication;
 import org.springframework.boot.autoconfigure.SpringBootApplication;
 
 import edu.eci.arsw.blueprints.model.Blueprint;
 import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
 import edu.eci.arsw.blueprints.persistence.impl.InMemoryBlueprintPersistence;
 import edu.eci.arsw.blueprints.services.BlueprintsServices;
 
 @SpringBootApplication
 public class BlueprintApplication {

 public static void main(String[] args) {
  Blueprint bp = new Blueprint("dsbaenar","blueprint1");
  InMemoryBlueprintPersistence ibp = new InMemoryBlueprintPersistence();
  BlueprintsServices bps = new BlueprintsServices(ibp);
  bps.addNewBlueprint(bp);
  try {
   System.out.println(bps.getBlueprint("dsbaenar", "blueprint1"));
  } catch (BlueprintNotFoundException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
 }

 }

  ```

* Configurar la aplicación -mediante anotaciones- para que el esquema de persistencia sea inyectado al momento de ser creado el bean 'BlueprintServices'.

 ```java
  @Service
 public class BlueprintsServices {
   
    @Autowired
    BlueprintsPersistence bpp=null;
  }
    
  ```

3. Complete los operaciones getBluePrint() y getBlueprintsByAuthor(). Implemente todo lo requerido de las capas inferiores (por ahora, el esquema de persistencia disponible 'InMemoryBlueprintPersistence') agregando las pruebas correspondientes en 'InMemoryPersistenceTest'.

   ```java
   public Blueprint addNewBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        bpp.saveBlueprint(bp); 
        return bp;
    }

    public Set<Blueprint> getAllBlueprints() throws BlueprintPersistenceException {
        if (bpp != null) {
            return bpp.getAllBlueprints();
        }
        throw new BlueprintPersistenceException("There are no Blueprints.");
    }
   ```

   Durante el desarrollo me dí cuenta que había un constructor de name y author mal implementado en el modelo, agregué this.author = author

   ```java
    public Blueprint(String author, String name){
        this.name=name;
        this.author=author;
        points=new ArrayList<>();
    }
   ```

4. Haga un programa en el que cree (mediante Spring) una instancia de BlueprintServices, y rectifique la funcionalidad del mismo: registrar planos, consultar planos, registrar planos específicos, etc.

    Rutas base: `https://blueprints-e2f7gcd4c0aqcsg2.canadacentral-01.azurewebsites.net/api/blueprints`

    Formato JSON de un blueprint

    ```json
    {
    "author": "alice",
    "name": "route1",
    "points": [
        {"x": 0, "y": 0},
        {"x": 0, "y": 0},
        {"x": 1, "y": 1},
        {"x": 2, "y": 2},
        {"x": 2, "y": 2},
        {"x": 3, "y": 3},
        {"x": 4, "y": 4}
    ]
    }
    ```

    Endpoints

    * Crear un blueprint
    * Método: POST
    * URL: /api/blueprints
    * Headers: Content-Type: application/json
    * Body: JSON del blueprint (ver arriba)
    * Respuesta: 201 Created (o 200 OK) con mensaje de confirmación
    * Errores: 409 Conflict si ya existe (BlueprintPersistenceException)

    Ejemplo curl:

    ```sh
    curl -X POST "https://blueprints-e2f7gcd4c0aqcsg2.canadacentral-01.azurewebsites.net/api/blueprints" \
        -H "Content-Type: application/json" \
        -d '@blueprint.json'
    ```

    * Obtener todos los blueprints
    * Método: GET
    * URL: /api/blueprints
    * Respuesta: 200 OK con array JSON de blueprints (si hay un filtro activo, devuelve las versiones filtradas)

    Ejemplo:

    ```sh
    curl -X GET "http://localhost:8080/api/blueprints" -H "Accept: application/json"
    ```

    * Obtener un blueprint por nombre
    * Método: GET
    * URL: /api/blueprints/blueprint/{name}
    * Respuesta: 200 OK con JSON del blueprint (filtrado si hay filtro activo)
    * Error: 404 Not Found si no existe

    Ejemplo:

    ```sh
    curl -X GET "http://localhost:8080/api/blueprints/blueprint/route1" -H "Accept: application/json"
    ```

    * Obtener blueprints por autor
    * Método: GET
    * URL: /api/blueprints/author/{author}
    * Respuesta: 200 OK con conjunto/array de blueprints del autor (aplica filtro si está activo)
    * Error: 404 Not Found si no hay planos del autor

    Ejemplo:

    ```sh
    curl -X GET "http://localhost:8080/api/blueprints/author/alice" -H "Accept: application/json"
    ```

    Códigos de estado importantes
    * 200 OK — petición satisfactoria (GET, PUT)
    * 201 Created — recurso creado (POST)
    * 404 Not Found — recurso no encontrado (BlueprintNotFoundException)
    * 409 Conflict — intento de crear blueprint que ya existe (BlueprintPersistenceException)
    * 400 Bad Request — petición mal formada

    Consejos de prueba
    * Primero POST el blueprint de prueba.
    * GET para verificar que se almacenó correctamente.

5. Se quiere que las operaciones de consulta de planos realicen un proceso de filtrado, antes de retornar los planos consultados. Dichos filtros lo que buscan es reducir el tamaño de los planos, removiendo datos redundantes o simplemente submuestrando, antes de retornarlos. Ajuste la aplicación (agregando las abstracciones e implementaciones que considere) para que a la clase BlueprintServices se le inyecte uno de dos posibles 'filtros' (o eventuales futuros filtros). No se contempla el uso de más de uno a la vez:

* (A) Filtrado de redundancias: suprime del plano los puntos consecutivos que sean repetidos.
* (B) Filtrado de submuestreo: suprime 1 de cada 2 puntos del plano, de manera intercalada.

Resumen de filtros implementados

* A) Filtrado de redundancias (RedundancyFilter)
  * Suprime puntos consecutivos iguales: [(0,0),(0,0),(1,1)] -> [(0,0),(1,1)]
* B) Filtrado de submuestreo (SubsamplingFilter)
  * Suprime 1 de cada 2 puntos (mantiene índices pares 0,2,4,...): [(0,0),(1,1),(2,2),(3,3)] -> [(0,0),(2,2)]

    Inyección y switching en BlueprintServices

* Constructor de `BlueprintsServices` recibe por inyección los dos filtros y mantiene una referencia al filtro activo:
  * private final BlueprintFilter redundancyFilter;
  * private final BlueprintFilter subsamplingFilter;
  * private BlueprintFilter activeFilter (sin filtrar)
* Método público para cambiar el filtro en runtime:
  * public void addFilter(String filterName)
    * "redundancy" -> activa redundancyFilter
    * "subsampling" / "subsample" -> activa subsamplingFilter
    * null o "none" -> desactiva filtros
    * lanza IllegalArgumentException si el nombre no es reconocido
* Aplicación del filtro en getters:
  * getBlueprint(...): obtiene blueprint de persistencia y retorna activeFilter != null ? activeFilter.filter(bp) : bp
  * getAllBlueprints(), getBlueprintsByAuthor(...), getBlueprintsByName(...) aplican el mismo mapeo sobre los resultados

```java
public interface BlueprintFilter {
    Blueprint filter(Blueprint bp);
}
```

```java
public class BlueprintsServices {

    private final BlueprintsPersistence bpp;
    private final BlueprintFilter redundancyFilter;
    private final BlueprintFilter subsamplingFilter;
    private BlueprintFilter activeFilter;

    public BlueprintsServices(BlueprintsPersistence bpp,
                              @Qualifier("redundancyFilter") BlueprintFilter redundancyFilter,
                              @Qualifier("subsamplingFilter") BlueprintFilter subsamplingFilter) {
        this.bpp = bpp;
        this.redundancyFilter = redundancyFilter;
        this.subsamplingFilter = subsamplingFilter;
        this.activeFilter = redundancyFilter;
    }

    public void addFilter(String filterName) {
        if (filterName == null || filterName.equalsIgnoreCase("none") || filterName.equalsIgnoreCase("off")) {
            this.activeFilter = null;
            return;
        }
        switch (filterName.trim().toLowerCase()) {
            case "redundancy":
            case "redundancyfilter":
                this.activeFilter = redundancyFilter;
                break;
            case "subsample":
            case "subsampling":
            case "subsamplingfilter":
                this.activeFilter = subsamplingFilter;
                break;
            default:
                throw new IllegalArgumentException("Filtro desconocido: " + filterName);
        }
    }

    public void updateBlueprint(String name, String filter) throws BlueprintNotFoundException, BlueprintPersistenceException {
        Set<Blueprint> existingBpSet = getBlueprintsByName(name);
        if (existingBpSet == null || existingBpSet.isEmpty()) {
            throw new BlueprintNotFoundException("No Blueprint with name " + name + " exists.");
        }

        Blueprint bp = existingBpSet.iterator().next();
        if (!bp.getName().equals(name)) {
            throw new BlueprintPersistenceException("The name of the updated blueprint must match the existing one.");
        }
        if (filter != null) {
            addFilter(filter);
            if (bpf != null) {
                bp = bpf.filter(bp);
            }
        }
        bpp.updateBlueprint(bp);
    }
}
```

Endpoints / uso desde REST

* Activar filtro por nombre (ejemplo controller):
  * PUT /api/blueprints/blueprint/{name}/filter/{filterName}
  * Llama a service.addFilter(filterName) y, si se desea persistir la versión filtrada, llama a service.updateBlueprint(...) después de aplicar el filtro.
* Ejemplos:
  * PUT .../filter/redundancy
  * PUT .../filter/subsampling



6. Agrege las pruebas correspondientes a cada uno de estos filtros, y pruebe su funcionamiento en el programa de prueba, comprobando que sólo cambiando la posición de las anotaciones -sin cambiar nada más-, el programa retorne los planos filtrados de la manera (A) o de la manera (B).

Pruebas unitarias sugeridas

* Test RedundancyFilter:
  * Input: [(0,0),(0,0),(1,1),(1,1),(2,2)]
  * Expected: [(0,0),(1,1),(2,2)]
* Test SubsamplingFilter:
  * Input: [(0,0),(1,1),(2,2),(3,3),(4,4)]
  * Expected (pares): [(0,0),(2,2),(4,4)]
* Test BlueprintsServices:
  * Inyectar InMemoryBlueprintPersistence y ambos filtros.
  * Verificar que addFilter cambia el comportamiento de getBlueprint/getAllBlueprints sin reiniciar la app.
  * Verificar comportamiento cuando activeFilter == null (sin filtrar).
  * Verificar que updateBlueprint persiste la versión filtrada (si esa opción fue implementada).
