# Ejercicio 5: Patrón Adapter Aplicado

## 🎯 Problema Identificado

### Situación: Recibir datos de APIs externas con formatos distintos

El endpoint `POST /api/battle/start/external` recibía datos con formato "extraño":

```json
{
  "fighter1_name": "Héroe",
  "fighter1_hp": 150,
  "fighter1_atk": 25,
  "fighter2_name": "Goblin",
  "fighter2_hp": 80,
  "fighter2_atk": 15
}
```

### Código Antes de la Refactorización

```java
@PostMapping("/start/external")
public ResponseEntity<Map<String, Object>> startBattleFromExternal(@RequestBody Map<String, Object> body) {
    // ❌ Conversión manual en el controller - código sucio y acoplado
    String fighter1Name = (String) body.getOrDefault("fighter1_name", "Héroe");
    int fighter1Hp = ((Number) body.getOrDefault("fighter1_hp", 150)).intValue();
    int fighter1Atk = ((Number) body.getOrDefault("fighter1_atk", 25)).intValue();
    String fighter2Name = (String) body.getOrDefault("fighter2_name", "Dragón");
    int fighter2Hp = ((Number) body.getOrDefault("fighter2_hp", 120)).intValue();
    int fighter2Atk = ((Number) body.getOrDefault("fighter2_atk", 30)).intValue();

    var result = battleService.startBattleFromExternal(
            fighter1Name, fighter1Hp, fighter1Atk,
            fighter2Name, fighter2Hp, fighter2Atk
    );
    // ...
}
```

### Problemas del Diseño Anterior

1. **❌ Lógica de conversión en el controller**: Ensucia la capa de presentación con lógica de transformación
2. **❌ No escalable**: Si llega un nuevo proveedor con formato diferente, debes modificar el controller
3. **❌ Código duplicado**: Cada endpoint que reciba datos externos repetiría esta lógica
4. **❌ Difícil de testear**: La conversión está acoplada al framework web
5. **❌ Viola Single Responsibility**: El controller hace presentación Y transformación de datos
6. **❌ Formato hardcodeado**: No hay forma de soportar múltiples formatos simultáneamente

### Escenario Problemático: Llega un Nuevo Proveedor

Mañana llega un nuevo proveedor con formato distinto:

```json
{
  "player": {
    "name": "Mago",
    "health": 100,
    "attack": 30,
    "defense": 8,
    "speed": 15
  },
  "enemy": {
    "name": "Dragón",
    "health": 200,
    "attack": 40,
    "defense": 20,
    "speed": 10
  }
}
```

**¿Qué tendrías que hacer sin el patrón Adapter?**
- ❌ Modificar el controller añadiendo otro `if/else` para detectar el formato
- ❌ Duplicar toda la lógica de conversión
- ❌ Aumentar complejidad ciclomática
- ❌ Romper el principio Open/Closed

---

## ✅ Solución: Patrón Adapter

### ¿Qué es el Patrón Adapter?

El patrón **Adapter** (también llamado **Wrapper**) permite que:
- Una interfaz **incompatible** se use como si fuera la esperada
- Conviertes el formato **externo** al formato de tu **dominio**
- Aíslas la lógica de conversión en clases dedicadas
- Añades nuevos formatos sin modificar código existente

**Analogía:** Es como un adaptador de enchufe que convierte 220V europeo a 110V americano — el dispositivo no sabe la diferencia.

---

## 🏗️ Arquitectura de la Solución

### 1. Interfaz común para todos los adapters

```java
public interface ExternalBattleDataAdapter {
    Character getPlayer();
    Character getEnemy();
    String getProviderName();
}
```

Esta interfaz define el **contrato** que todos los adapters deben cumplir.

### 2. Clases DTO para formatos externos

#### Provider 1: Formato "fighter1_*, fighter2_*"

```java
public class Provider1BattleData {
    private String fighter1_name;
    private int fighter1_hp;
    private int fighter1_atk;
    private int fighter1_def;
    private int fighter1_spd;
    
    private String fighter2_name;
    private int fighter2_hp;
    private int fighter2_atk;
    private int fighter2_def;
    private int fighter2_spd;
    
    // Getters y Setters
}
```

#### Provider 2: Formato "player/enemy objects"

```java
public class Provider2BattleData {
    private FighterData player;
    private FighterData enemy;

    public static class FighterData {
        private String name;
        private int health;
        private int attack;
        private int defense;
        private int speed;
        // Getters y Setters
    }
}
```

### 3. Adapters concretos

#### Adapter para Provider 1

```java
public class Provider1BattleAdapter implements ExternalBattleDataAdapter {
    private final Provider1BattleData data;

    public Provider1BattleAdapter(Provider1BattleData data) {
        this.data = data;
    }

    @Override
    public Character getPlayer() {
        return new Character.Builder(data.getFighter1_name())
                .maxHp(data.getFighter1_hp())
                .attack(data.getFighter1_atk())
                .defense(data.getFighter1_def())
                .speed(data.getFighter1_spd())
                .characterClass("EXTERNAL_PROVIDER_1")
                .build();
    }

    @Override
    public Character getEnemy() {
        return new Character.Builder(data.getFighter2_name())
                .maxHp(data.getFighter2_hp())
                .attack(data.getFighter2_atk())
                .defense(data.getFighter2_def())
                .speed(data.getFighter2_spd())
                .characterClass("EXTERNAL_PROVIDER_1")
                .build();
    }

    @Override
    public String getProviderName() {
        return "Provider1 (fighter1_*, fighter2_*)";
    }
}
```

#### Adapter para Provider 2

```java
public class Provider2BattleAdapter implements ExternalBattleDataAdapter {
    private final Provider2BattleData data;

    public Provider2BattleAdapter(Provider2BattleData data) {
        this.data = data;
    }

    @Override
    public Character getPlayer() {
        Provider2BattleData.FighterData playerData = data.getPlayer();
        return new Character.Builder(playerData.getName())
                .maxHp(playerData.getHealth())
                .attack(playerData.getAttack())
                .defense(playerData.getDefense())
                .speed(playerData.getSpeed())
                .characterClass("EXTERNAL_PROVIDER_2")
                .build();
    }

    @Override
    public Character getEnemy() {
        Provider2BattleData.FighterData enemyData = data.getEnemy();
        return new Character.Builder(enemyData.getName())
                .maxHp(enemyData.getHealth())
                .attack(enemyData.getAttack())
                .defense(enemyData.getDefense())
                .speed(enemyData.getSpeed())
                .characterClass("EXTERNAL_PROVIDER_2")
                .build();
    }

    @Override
    public String getProviderName() {
        return "Provider2 (player.health, enemy.health)";
    }
}
```

### 4. Adapter Genérico para Map (Transición)

Para facilitar la migración del código legacy que usa `Map<String, Object>`, creamos un adapter que detecta automáticamente el formato:

```java
public class MapBasedBattleAdapter implements ExternalBattleDataAdapter {
    private final ExternalBattleDataAdapter delegate;

    public MapBasedBattleAdapter(Map<String, Object> data) {
        this.delegate = detectAndCreateAdapter(data);
    }

    private ExternalBattleDataAdapter detectAndCreateAdapter(Map<String, Object> data) {
        // Detectar Provider 1: tiene fighter1_name, fighter1_hp, etc.
        if (data.containsKey("fighter1_name") || data.containsKey("fighter1_hp")) {
            return createProvider1Adapter(data);
        }
        
        // Detectar Provider 2: tiene player y enemy como objetos
        if (data.containsKey("player") && data.get("player") instanceof Map) {
            return createProvider2Adapter(data);
        }
        
        // Default: Provider 1
        return createProvider1Adapter(data);
    }

    @Override
    public Character getPlayer() {
        return delegate.getPlayer();
    }

    @Override
    public Character getEnemy() {
        return delegate.getEnemy();
    }

    @Override
    public String getProviderName() {
        return delegate.getProviderName() + " (via MapBasedAdapter)";
    }
}
```

---

## 🔄 Código Después de la Refactorización

### En BattleService

```java
/**
 * Inicia una batalla desde datos externos usando el patrón Adapter.
 */
public BattleStartResult startBattleFromAdapter(ExternalBattleDataAdapter adapter) {
    // El adapter se encarga de toda la conversión - código limpio y desacoplado
    Character player = adapter.getPlayer();
    Character enemy = adapter.getEnemy();

    Battle battle = new Battle(player, enemy);
    battle.log("Batalla iniciada desde " + adapter.getProviderName());
    
    String battleId = UUID.randomUUID().toString();
    battleRepository.save(battleId, battle);
    
    return new BattleStartResult(battleId, battle);
}
```

### En BattleController

```java
/**
 * Endpoint refactorizado: usa el patrón Adapter.
 * Soporta múltiples formatos automáticamente.
 */
@PostMapping("/start/external")
public ResponseEntity<Map<String, Object>> startBattleFromExternal(@RequestBody Map<String, Object> body) {
    // ✅ Patrón Adapter: delega la conversión al adapter
    // El controller no necesita saber los detalles del formato externo
    MapBasedBattleAdapter adapter = new MapBasedBattleAdapter(body);
    
    var result = battleService.startBattleFromAdapter(adapter);
    Battle battle = result.battle();

    return ResponseEntity.ok(Map.of(
            "battleId", result.battleId(),
            "player", toCharacterDto(battle.getPlayer()),
            "enemy", toCharacterDto(battle.getEnemy()),
            "currentTurn", battle.getCurrentTurn(),
            "battleLog", battle.getBattleLog(),
            "finished", battle.isFinished(),
            "playerAttacks", BattleService.PLAYER_ATTACKS,
            "lastDamage", 0,
            "lastDamageTarget", ""
    ));
}
```

---

## 📊 Comparación: Antes vs Después

### ❌ Antes

```java
// Controller conoce el formato externo y hace la conversión manualmente
String fighter1Name = (String) body.getOrDefault("fighter1_name", "Héroe");
int fighter1Hp = ((Number) body.getOrDefault("fighter1_hp", 150)).intValue();
int fighter1Atk = ((Number) body.getOrDefault("fighter1_atk", 25)).intValue();
String fighter2Name = (String) body.getOrDefault("fighter2_name", "Dragón");
int fighter2Hp = ((Number) body.getOrDefault("fighter2_hp", 120)).intValue();
int fighter2Atk = ((Number) body.getOrDefault("fighter2_atk", 30)).intValue();

var result = battleService.startBattleFromExternal(
        fighter1Name, fighter1Hp, fighter1Atk,
        fighter2Name, fighter2Hp, fighter2Atk
);

// Para añadir Provider 2:
// - Modificar el controller con otro if/else
// - Duplicar lógica de conversión
// - Aumentar complejidad
```

### ✅ Después

```java
// Controller NO conoce los detalles del formato - solo usa el adapter
MapBasedBattleAdapter adapter = new MapBasedBattleAdapter(body);
var result = battleService.startBattleFromAdapter(adapter);

// Para añadir Provider 3:
// - Crear Provider3BattleData (DTO)
// - Crear Provider3BattleAdapter implements ExternalBattleDataAdapter
// - Actualizar MapBasedBattleAdapter para detectar el nuevo formato
// - ¡El controller y service NO CAMBIAN!
```

---

## 🎁 Ventajas del Patrón Adapter

### 1. **Separación de Responsabilidades**
- ✅ Controller: Solo maneja HTTP request/response
- ✅ Adapter: Solo hace conversión de formato
- ✅ Service: Solo maneja lógica de negocio

### 2. **Open/Closed Principle**
```java
// Añadir Provider 3 sin modificar código existente
public class Provider3BattleAdapter implements ExternalBattleDataAdapter {
    // Nueva implementación - código existente intacto
}
```

### 3. **Facilita Testing**
```java
@Test
public void testProvider1Adapter() {
    Provider1BattleData data = new Provider1BattleData(
        "Héroe", 100, 20, 10, 10,
        "Goblin", 50, 15, 5, 5
    );
    
    Provider1BattleAdapter adapter = new Provider1BattleAdapter(data);
    
    Character player = adapter.getPlayer();
    assertEquals("Héroe", player.getName());
    assertEquals(100, player.getMaxHp());
}
```

### 4. **Reutilización**
```java
// Puedes usar el mismo adapter en diferentes endpoints
@PostMapping("/tournament/external")
public void startTournament(@RequestBody Map<String, Object> body) {
    MapBasedBattleAdapter adapter = new MapBasedBattleAdapter(body);
    // Reutilizar el adapter
}
```

### 5. **Desacoplamiento**
- El controller no conoce los formatos externos
- El service no conoce los formatos externos
- Solo los adapters conocen la conversión

---

## 🧪 Ejemplos de Uso

### Ejemplo 1: Provider 1 (formato actual)

**Request:**
```json
POST /api/battle/start/external
{
  "fighter1_name": "Caballero",
  "fighter1_hp": 180,
  "fighter1_atk": 30,
  "fighter1_def": 20,
  "fighter1_spd": 15,
  "fighter2_name": "Orco",
  "fighter2_hp": 100,
  "fighter2_atk": 25,
  "fighter2_def": 10,
  "fighter2_spd": 12
}
```

**Procesamiento:**
1. `MapBasedBattleAdapter` detecta formato Provider 1
2. Crea `Provider1BattleAdapter`
3. Convierte a `Character` usando Builder
4. Batalla iniciada ✅

### Ejemplo 2: Provider 2 (nuevo formato)

**Request:**
```json
POST /api/battle/start/external
{
  "player": {
    "name": "Arquero",
    "health": 120,
    "attack": 35,
    "defense": 12,
    "speed": 25
  },
  "enemy": {
    "name": "Esqueleto",
    "health": 60,
    "attack": 18,
    "defense": 5,
    "speed": 10
  }
}
```

**Procesamiento:**
1. `MapBasedBattleAdapter` detecta formato Provider 2
2. Crea `Provider2BattleAdapter`
3. Convierte a `Character` usando Builder
4. Batalla iniciada ✅

**¡El mismo endpoint maneja ambos formatos sin modificaciones!**

---

## 🔍 Diagrama de Clases

```
┌─────────────────────────────────┐
│ ExternalBattleDataAdapter       │
│ (Interface)                     │
├─────────────────────────────────┤
│ + getPlayer(): Character        │
│ + getEnemy(): Character         │
│ + getProviderName(): String     │
└─────────────────────────────────┘
           ▲         ▲         ▲
           │         │         │
           │         │         │
┌──────────┴───┐   ┌─┴──────────┐   ┌─────┴─────────┐
│ Provider1    │   │ Provider2  │   │ MapBased      │
│ Adapter      │   │ Adapter    │   │ Adapter       │
├──────────────┤   ├────────────┤   ├───────────────┤
│ - data       │   │ - data     │   │ - delegate    │
│              │   │            │   │ - detecta el  │
│ Convierte    │   │ Convierte  │   │   formato y   │
│ fighter1_*   │   │ player{}   │   │   delega      │
└──────────────┘   └────────────┘   └───────────────┘
```

---

## 📝 Cuándo Usar el Patrón Adapter

### ✅ Usar Adapter cuando:
- Necesitas integrar **sistemas externos** con formatos distintos
- Recibes datos de **múltiples proveedores**
- Quieres **aislar la conversión** de formatos
- El formato externo **no coincide** con tu dominio
- Necesitas **reutilizar código existente** con nuevas interfaces
- Implementas **integraciones con APIs de terceros**

### ❌ NO usar Adapter cuando:
- El formato externo ya coincide con tu dominio
- Solo tienes un único formato que nunca cambiará
- La conversión es trivial (mapeo 1:1)
- Estás en control del formato (puedes cambiarlo directamente)

---

## 🎓 Adapter vs Otros Patrones

### Adapter vs Facade
- **Adapter**: Convierte UNA interfaz en OTRA esperada
- **Facade**: Simplifica MÚLTIPLES interfaces complejas en una simple

### Adapter vs Decorator
- **Adapter**: Cambia la interfaz (conversión)
- **Decorator**: Mantiene la interfaz pero añade funcionalidad

### Adapter vs Bridge
- **Adapter**: Se usa después (retrofit de código existente)
- **Bridge**: Se diseña antes (separar abstracción e implementación)

---

## 🚀 Ventajas en Nuestro Proyecto

### Extensibilidad
```java
// Añadir Provider 3 (formato XML, JSON diferente, etc.)
public class Provider3BattleAdapter implements ExternalBattleDataAdapter {
    // Implementación nueva
    // ¡Controller y Service NO CAMBIAN!
}
```

### Testing Aislado
```java
// Testear adapters independientemente
@Test
public void testAdapter() {
    Provider1BattleData mockData = createMockData();
    Provider1BattleAdapter adapter = new Provider1BattleAdapter(mockData);
    
    Character result = adapter.getPlayer();
    // Verificar conversión
}
```

### Claridad
```java
// Controller: "No sé qué formato es, el adapter lo maneja"
MapBasedBattleAdapter adapter = new MapBasedBattleAdapter(body);
var result = battleService.startBattleFromAdapter(adapter);

// Claro, conciso, desacoplado ✅
```

---

## 📊 Resultado Final

### Archivos Creados
1. `ExternalBattleDataAdapter.java` - Interfaz común
2. `Provider1BattleData.java` - DTO Provider 1
3. `Provider2BattleData.java` - DTO Provider 2
4. `Provider1BattleAdapter.java` - Adapter Provider 1
5. `Provider2BattleAdapter.java` - Adapter Provider 2
6. `MapBasedBattleAdapter.java` - Adapter genérico con auto-detección

### Archivos Modificados
1. `BattleService.java` - Método `startBattleFromAdapter()`
2. `BattleController.java` - Endpoint refactorizado

### Beneficios Conseguidos
- ✅ Controller limpio (solo HTTP)
- ✅ Conversión aislada (adapters)
- ✅ Extensible (nuevos formatos sin modificar código)
- ✅ Testable (adapters independientes)
- ✅ Cumple Open/Closed Principle
- ✅ Cumple Single Responsibility Principle

---

## 🔚 Conclusión

El patrón **Adapter** resuelve el problema de integración con sistemas externos que usan formatos diferentes. En lugar de contaminar el controller o service con lógica de conversión, encapsulamos cada formato en su propio adapter que convierte al modelo de dominio.

**Resultado:** Código más limpio, mantenible, testable y extensible. Añadir nuevos proveedores es tan simple como crear un nuevo adapter — el resto del sistema permanece intacto.

