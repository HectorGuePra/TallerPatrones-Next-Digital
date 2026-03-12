# Ejercicio 3: Patrón Builder Aplicado

## 🎯 Problema Identificado

### Antes de la Refactorización
```java
// Constructor con muchos parámetros - difícil de leer y mantener
Character player = new Character("Héroe", 150, 25, 15, 20);
// ¿Qué significa cada número? ¿150 es HP? ¿25 es ataque?

// Al añadir más atributos (equipamiento, clase, buffos):
Character player = new Character("Héroe", 150, 25, 15, 20, "Espada", "Warrior", false, 0, 0);
// Imposible de leer y mantener
```

### Problemas del Constructor con Muchos Parámetros
1. **❌ No legible**: No sabes qué significa cada parámetro sin ver la firma del método
2. **❌ Orden rígido**: Debes pasar todos los parámetros en orden exacto
3. **❌ Sin valores por defecto**: Debes especificar todos los valores, incluso los opcionales
4. **❌ Difícil de mantener**: Añadir un parámetro rompe todo el código existente
5. **❌ Propenso a errores**: Fácil confundir el orden de parámetros similares (int, int, int...)

---

## ✅ Solución: Patrón Builder

### Implementación

```java
public class Character {
    // Atributos...
    private final String name;
    private final int maxHp;
    private final int attack;
    private final String equipment;
    private final String characterClass;
    private final boolean hasBuff;

    // Constructor privado - solo accesible desde el Builder
    private Character(Builder builder) {
        this.name = builder.name;
        this.maxHp = builder.maxHp;
        this.attack = builder.attack;
        // ...inicializar todos los campos
    }

    // Clase Builder interna
    public static class Builder {
        // Campo obligatorio
        private final String name;
        
        // Campos opcionales con valores por defecto
        private int maxHp = 100;
        private int attack = 10;
        private int defense = 10;
        private int speed = 10;
        private String equipment = null;
        private String characterClass = "WARRIOR";
        private boolean hasBuff = false;

        public Builder(String name) {
            this.name = name;
        }

        public Builder maxHp(int maxHp) {
            this.maxHp = maxHp;
            return this;
        }

        public Builder attack(int attack) {
            this.attack = attack;
            return this;
        }

        // ... otros métodos setter que retornan 'this'

        public Character build() {
            return new Character(this);
        }
    }
}
```

### Después de la Refactorización

```java
// ✅ Código legible y auto-documentado
Character player = new Character.Builder("Héroe")
    .maxHp(150)          // Claramente es HP máximo
    .attack(25)          // Claramente es ataque
    .defense(15)         // Claramente es defensa
    .speed(20)           // Claramente es velocidad
    .characterClass("WARRIOR")
    .equipment("Espada Básica")
    .build();

// ✅ Puedes omitir parámetros opcionales
Character simpleEnemy = new Character.Builder("Goblin")
    .maxHp(50)
    .attack(10)
    .build();  // defense, speed, etc. usan valores por defecto

// ✅ El orden no importa
Character mage = new Character.Builder("Mago")
    .equipment("Bastón Mágico")
    .maxHp(80)
    .characterClass("MAGE")
    .speed(15)
    .attack(35)
    .build();
```

---

## 🎁 Ventajas del Patrón Builder

### 1. **Legibilidad**
```java
// ❌ Antes: ¿Qué es cada número?
new Character("Héroe", 150, 25, 15, 20);

// ✅ Después: Auto-documentado
new Character.Builder("Héroe")
    .maxHp(150)
    .attack(25)
    .defense(15)
    .speed(20)
    .build();
```

### 2. **Valores por Defecto**
```java
// Solo especificas lo que necesitas cambiar
Character enemy = new Character.Builder("Dragón")
    .maxHp(200)
    .attack(40)
    .build();  // defense, speed, etc. usan valores predeterminados
```

### 3. **Validación Centralizada**
```java
public Builder maxHp(int maxHp) {
    if (maxHp <= 0) {
        throw new IllegalArgumentException("maxHp debe ser positivo");
    }
    this.maxHp = maxHp;
    return this;
}
```

### 4. **Inmutabilidad**
El constructor privado + campos final = objeto inmutable después de construido.

### 5. **Extensibilidad**
```java
// Añadir un nuevo atributo no rompe el código existente
public Builder level(int level) {
    this.level = level;
    return this;
}

// El código existente sigue funcionando sin cambios
Character old = new Character.Builder("Viejo").maxHp(100).build();

// Nuevo código puede usar el nuevo atributo
Character newChar = new Character.Builder("Nuevo")
    .maxHp(100)
    .level(10)  // Nuevo atributo
    .build();
```

---

## 📊 Comparación: Antes vs Después

### En BattleService.java

#### ❌ Antes (Constructor Tradicional)
```java
Character player = new Character(
    playerName != null ? playerName : "Héroe",
    150, 25, 15, 20  // ¿Qué significa cada número?
);
```

#### ✅ Después (Patrón Builder)
```java
Character player = new Character.Builder(playerName != null ? playerName : "Héroe")
    .maxHp(150)       // Claro: HP máximo
    .attack(25)       // Claro: ataque
    .defense(15)      // Claro: defensa
    .speed(20)        // Claro: velocidad
    .characterClass("WARRIOR")
    .equipment("Espada Básica")
    .build();
```

### En startBattleFromExternal()

#### ❌ Antes
```java
Character player = new Character(fighter1Name, fighter1Hp, fighter1Atk, 10, 10);
// Conversión confusa de datos externos
```

#### ✅ Después
```java
Character player = new Character.Builder(fighter1Name)
    .maxHp(fighter1Hp)
    .attack(fighter1Atk)
    .defense(10)
    .speed(10)
    .characterClass("EXTERNAL")
    .build();
// Conversión clara y explícita
```

---

## 🔧 Compatibilidad Mantenida

Para no romper el código existente, mantuvimos el constructor legacy:

```java
// Constructor legacy - para compatibilidad con código antiguo
public Character(String name, int maxHp, int attack, int defense, int speed) {
    this.name = name;
    this.maxHp = maxHp;
    this.currentHp = maxHp;
    this.attack = attack;
    this.defense = defense;
    this.speed = speed;
    this.equipment = null;
    this.characterClass = "WARRIOR";
    this.hasBuff = false;
}
```

Esto permite:
- ✅ El código viejo sigue funcionando
- ✅ El código nuevo puede usar el Builder
- ✅ Migración gradual sin romper nada

---

## 🎓 Cuándo Usar el Patrón Builder

### ✅ Usar Builder cuando:
- Tienes **4+ parámetros** en el constructor
- Muchos parámetros son **opcionales**
- Necesitas **valores por defecto**
- Quieres **validación** durante la construcción
- El objeto debe ser **inmutable** después de creado
- Necesitas **legibilidad** en la creación del objeto

### ❌ NO usar Builder cuando:
- El objeto tiene **1-3 parámetros** simples
- Todos los parámetros son **obligatorios** y **simples**
- La construcción es **trivial** sin lógica compleja
- Necesitas **performance extremo** (Builder añade overhead mínimo)

---

## 🚀 Ejecución y Prueba

```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación
mvn spring-boot:run

# Probar en el navegador
http://localhost:8080
```

---

## 📝 Conclusión

El patrón **Builder** resuelve el problema de constructores telescópicos y hace el código:
- **Más legible**: Sabes qué estás configurando
- **Más mantenible**: Añadir parámetros no rompe el código
- **Más flexible**: Valores por defecto y parámetros opcionales
- **Más seguro**: Validación centralizada y objetos inmutables

Este patrón es especialmente útil en clases de dominio como `Character` que pueden tener muchos atributos opcionales (equipamiento, buffos, estadísticas especiales, etc.).

