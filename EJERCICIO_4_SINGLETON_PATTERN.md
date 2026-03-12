# Ejercicio 4: Patrón Singleton Aplicado

## 🎯 Problema Identificado

### Antes de la Refactorización

```java
public class BattleRepository {
    private static final Map<String, Battle> battles = new ConcurrentHashMap<>();
    
    // Constructor público - cualquiera puede hacer new
    public BattleRepository() {}
    
    public void save(String id, Battle battle) {
        battles.put(id, battle);
    }
}

// En BattleService
public class BattleService {
    private final BattleRepository battleRepository = new BattleRepository();
    // Cada servicio crea su propia instancia
}
```

### Problemas del Diseño Anterior

1. **❌ Múltiples instancias**: Aunque el `Map` era estático, cada clase podía crear su propia instancia de `BattleRepository`
2. **❌ Confusión conceptual**: ¿Por qué tener múltiples repositorios si comparten el mismo `Map`?
3. **❌ Sin control de acceso**: Cualquiera podía hacer `new BattleRepository()`
4. **❌ Dificulta testing**: No hay forma de resetear o controlar la instancia
5. **❌ Gestión de memoria**: Múltiples instancias innecesarias aunque compartan datos

### Escenario Problemático

```java
// BattleService crea su repositorio
BattleService battleService = new BattleService();
battleService.battleRepository = new BattleRepository(); // Instancia 1

// Si crean un TournamentService
TournamentService tournamentService = new TournamentService();
tournamentService.battleRepository = new BattleRepository(); // Instancia 2

// Aunque comparten el Map estático, tener 2 instancias es confuso
// ¿Son el mismo repositorio? ¿Comparten datos?
// La respuesta debería ser clara: UNA ÚNICA INSTANCIA
```

---

## ✅ Solución: Patrón Singleton

### ¿Qué es el Patrón Singleton?

El patrón **Singleton** garantiza que:
- ✅ Una clase tiene **una única instancia** en toda la aplicación
- ✅ Proporciona un **punto de acceso global** a esa instancia
- ✅ El constructor es **privado** para evitar instanciación directa
- ✅ La clase controla su propia instanciación

### Implementación

```java
public class BattleRepository {

    // 1. Instancia única (volatile para thread-safety)
    private static volatile BattleRepository instance;
    
    // 2. Almacenamiento de datos (ahora NO es estático)
    private final Map<String, Battle> battles = new ConcurrentHashMap<>();

    // 3. Constructor privado - nadie puede hacer new desde fuera
    private BattleRepository() {
        // Constructor privado - patrón Singleton
    }

    // 4. Método público para obtener la instancia única
    public static BattleRepository getInstance() {
        if (instance == null) {
            synchronized (BattleRepository.class) {
                if (instance == null) {
                    instance = new BattleRepository();
                }
            }
        }
        return instance;
    }

    // Métodos de negocio
    public void save(String id, Battle battle) {
        battles.put(id, battle);
    }

    public Battle findById(String id) {
        return battles.get(id);
    }
}
```

### Después de la Refactorización

```java
// En BattleService
public class BattleService {
    // Usa la instancia única del repositorio
    private final BattleRepository battleRepository = BattleRepository.getInstance();
}

// En cualquier otro servicio
public class TournamentService {
    // Obtiene LA MISMA instancia
    private final BattleRepository battleRepository = BattleRepository.getInstance();
}

// Todos usan la misma instancia
BattleRepository repo1 = BattleRepository.getInstance();
BattleRepository repo2 = BattleRepository.getInstance();
System.out.println(repo1 == repo2); // true - misma instancia
```

---

## 🔒 Thread-Safety: Double-Checked Locking

### ¿Por qué `volatile` y `synchronized`?

En entornos multi-threaded (como Spring Boot), múltiples requests podrían intentar obtener la instancia simultáneamente.

```java
public static BattleRepository getInstance() {
    // Primera verificación sin lock (rápida)
    if (instance == null) {
        // Lock solo si instance es null
        synchronized (BattleRepository.class) {
            // Segunda verificación dentro del lock
            if (instance == null) {
                instance = new BattleRepository();
            }
        }
    }
    return instance;
}
```

**Ventajas:**
- ✅ **Lazy initialization**: Solo se crea cuando se necesita
- ✅ **Thread-safe**: No hay condiciones de carrera
- ✅ **Performance**: El lock solo se usa la primera vez
- ✅ **volatile**: Garantiza visibilidad entre threads

### Alternativa: Initialization-on-demand holder

```java
public class BattleRepository {
    
    private BattleRepository() {}
    
    // Holder interno - se carga cuando se accede
    private static class Holder {
        private static final BattleRepository INSTANCE = new BattleRepository();
    }
    
    public static BattleRepository getInstance() {
        return Holder.INSTANCE;
    }
}
```

Esta es la forma más eficiente y thread-safe (usando el class loader de Java).

---

## 🎁 Ventajas del Patrón Singleton

### 1. **Una Única Instancia Garantizada**
```java
BattleRepository repo1 = BattleRepository.getInstance();
BattleRepository repo2 = BattleRepository.getInstance();
BattleRepository repo3 = BattleRepository.getInstance();

// Todas son la misma instancia
assert repo1 == repo2 && repo2 == repo3;
```

### 2. **Control de Acceso**
```java
// ❌ Ya no puedes hacer esto
// BattleRepository repo = new BattleRepository(); // Error de compilación

// ✅ Solo hay una forma correcta
BattleRepository repo = BattleRepository.getInstance();
```

### 3. **Punto de Acceso Global**
```java
// Desde cualquier parte de la aplicación
BattleRepository.getInstance().save(id, battle);
BattleRepository.getInstance().findById(id);
```

### 4. **Facilita Testing**
```java
@After
public void tearDown() {
    // Puedes limpiar el estado compartido
    BattleRepository.getInstance().clear();
}
```

### 5. **Gestión Eficiente de Recursos**
- Solo una instancia en memoria
- Inicialización perezosa (lazy)
- Ideal para conexiones de BD, caches, configuraciones, etc.

---

## 📊 Comparación: Antes vs Después

### ❌ Antes (Sin Singleton)

```java
// Cada servicio crea su propia instancia
public class BattleService {
    private final BattleRepository repo = new BattleRepository(); // Instancia 1
}

public class TournamentService {
    private final BattleRepository repo = new BattleRepository(); // Instancia 2
}

public class StatisticsService {
    private final BattleRepository repo = new BattleRepository(); // Instancia 3
}

// Resultado: 3 instancias diferentes
// Aunque compartan el Map estático, es confuso y mal diseño
```

### ✅ Después (Con Singleton)

```java
// Todos los servicios usan LA MISMA instancia
public class BattleService {
    private final BattleRepository repo = BattleRepository.getInstance(); // Instancia única
}

public class TournamentService {
    private final BattleRepository repo = BattleRepository.getInstance(); // Misma instancia
}

public class StatisticsService {
    private final BattleRepository repo = BattleRepository.getInstance(); // Misma instancia
}

// Resultado: 1 única instancia compartida
// Claro, consistente y eficiente
```

---

## ⚠️ Consideraciones y Cuidados

### 1. **Testing**
El Singleton puede dificultar los tests unitarios porque mantiene estado global.

**Solución:** Añadir métodos para testing
```java
public void clear() {
    battles.clear();
}

// En tests
@After
public void cleanup() {
    BattleRepository.getInstance().clear();
}
```

### 2. **Dependency Injection**
En frameworks modernos (Spring), se prefiere usar DI en lugar de Singleton.

```java
// En Spring, esto ya es Singleton por defecto
@Repository
public class BattleRepository {
    // Spring gestiona la instancia única
}

@Service
public class BattleService {
    @Autowired
    private BattleRepository repository; // Spring inyecta la misma instancia
}
```

### 3. **Serialización**
Si necesitas serializar el Singleton, debes implementar `readResolve`:

```java
protected Object readResolve() {
    return getInstance();
}
```

---

## 🎓 Cuándo Usar el Patrón Singleton

### ✅ Usar Singleton cuando:
- **Necesitas UNA ÚNICA instancia** de una clase en toda la aplicación
- Gestión de **recursos compartidos** (pools de conexiones, caches, registros)
- **Configuración global** de la aplicación
- **Coordinadores centrales** (gestores de logs, de eventos)
- **Repositorios en memoria** (como nuestro caso)

### ❌ NO usar Singleton cuando:
- Necesitas **múltiples instancias** con estados diferentes
- Estás usando **frameworks con DI** (usa las capacidades del framework)
- Necesitas **alta testabilidad** (Singleton puede complicar mocking)
- La clase no tiene estado compartido

---

## 🚀 Ventajas Específicas en Nuestro Proyecto

### Antes
```java
// Confusión: ¿Cuántas instancias hay?
BattleService service1 = new BattleService();
BattleService service2 = new BattleService();
// Cada uno tiene su BattleRepository, pero comparten Map estático 🤔
```

### Después
```java
// Claridad: Todos usan el mismo repositorio
BattleService service1 = new BattleService();
BattleService service2 = new BattleService();
// Ambos usan BattleRepository.getInstance() - LA MISMA instancia ✅

// Puedes verificarlo
BattleRepository repo1 = BattleRepository.getInstance();
BattleRepository repo2 = BattleRepository.getInstance();
System.out.println(repo1 == repo2); // true
```

---

## 📝 Conclusión

El patrón **Singleton** resuelve el problema de:
- ✅ Garantizar **una única instancia** de `BattleRepository`
- ✅ Proporcionar **acceso global** consistente
- ✅ **Controlar la instanciación** con constructor privado
- ✅ Ser **thread-safe** en entornos concurrentes
- ✅ Hacer el código más **claro y mantenible**

Ahora no hay ambigüedad: existe **UN ÚNICO repositorio de batallas** en toda la aplicación, y todos los servicios lo comparten correctamente.

---

## 🔍 Verificación

```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación
mvn spring-boot:run

# Probar en el navegador
http://localhost:8080
```

El sistema funciona igual que antes, pero ahora con un diseño más sólido y profesional.

