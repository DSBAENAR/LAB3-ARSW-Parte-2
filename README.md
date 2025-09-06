# Parte I

1. Modifique el bean de persistecia 'InMemoryBlueprintPersistence' para que por defecto se inicialice con al menos otros tres planos, y con dos asociados a un mismo autor.

```java
public InMemoryBlueprintPersistence() {
        Blueprint bp=new Blueprint("dsbaenar", "ECI Blueprint", new Point[]{new Point(140, 140),new Point(115, 115)});
        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        bp=new Blueprint("dsbaenar", "Highway Blueprint", new Point[]{new Point(10, 10),new Point(15, 15)});
        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        bp=new Blueprint("Maria", "House Blueprint", new Point[]{new Point(0, 0),new Point(10, 10)});
        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
    }
```

2. Configure su aplicación para que ofrezca el recurso "/blueprints", de manera que cuando se le haga una petición GET, retorne -en formato jSON- el conjunto de todos los planos. Para esto:
```java
@RestController
@RequestMapping("/api/blueprints")
/**
 * Controller for managing blueprints.
 */
public class BlueprintController {

    private final BlueprintsServices bps;

    /**
     * Constructor for BlueprintController.
     * @param bps the BlueprintsServices instance
     */
    public BlueprintController(BlueprintsServices bps) {
        this.bps = bps;
    }

    /**
     * Get all blueprints.
     * @return ResponseEntity
     */
    @GetMapping("")
    public ResponseEntity<?> getAllBlueprints() {
        try{
            return ResponseEntity.ok(bps.getAllBlueprints());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
```
