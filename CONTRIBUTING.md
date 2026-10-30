# Guía de contribución — refactorizacion-cine

Reglas del equipo para mantener el repo ordenado. **Léela antes de tu primer commit.**

## 1. Flujo de trabajo

1. Toma tu issue en el [tablero](https://github.com/users/Luis-villanueva-valdivia/projects/3) y muévelo a **In Progress**.
2. Actualiza `main` y crea tu rama desde ahí:
   ```bash
   git checkout main
   git pull origin main
   git checkout -b refactor/ISS-03-funcion
   ```
3. Aplica **una técnica por commit** (ver sección 3).
4. Antes de cada commit: compila, ejecuta y compara contra `salida_antes.txt`:
   ```bash
   javac *.java && java Main > salida_actual.txt
   diff salida_antes.txt salida_actual.txt   # en Windows: fc salida_antes.txt salida_actual.txt
   ```
   La salida debe ser **idéntica** (la única excepción es el bug de F7, en ISS-07).
5. Sube tu rama y abre un Pull Request hacia `main` (sección 4).
6. Espera la revisión de **@Luis-villanueva-valdivia**. Nadie hace merge sin esa aprobación.
7. Después del merge, borra tu rama y mueve el issue a **Done** (se cierra solo con `Closes #N`).

> ⚠️ **Nadie hace push directo a `main`.** Está protegida: todo entra por Pull Request.

## 2. Nombres de ramas

Formato:

```
tipo/ISS-XX-descripcion-corta
```

- `tipo` es el mismo que el label `tipo:` del issue: `refactor`, `fix`, `docs`, `test`, `chore`.
- `ISS-XX` es el número del issue, con dos dígitos.
- La descripción va en minúsculas, con guiones, sin acentos ni ñ, de 2 a 4 palabras.

| Issue | Rama |
|---|---|
| ISS-01 | `test/ISS-01-salida-antes` |
| ISS-02 | `docs/ISS-02-diagrama-original` |
| ISS-03 | `refactor/ISS-03-funcion` |
| ISS-04 | `refactor/ISS-04-reserva-busqueda` |
| ISS-05 | `refactor/ISS-05-precios` |
| ISS-06 | `refactor/ISS-06-firma-reservar` |
| ISS-07 | `fix/ISS-07-errores-y-bug` |
| ISS-08 | `docs/ISS-08-diagrama-refactorizado` |
| ISS-09 | `test/ISS-09-verificacion-final` |

## 3. Mensajes de commit

Formato ([Conventional Commits](https://www.conventionalcommits.org/es/), en español):

```
tipo(Clase): descripción en imperativo (PX)

Refs #N
```

- **tipo:** `refactor`, `fix`, `docs`, `test` o `chore`.
- **(Clase):** la clase que tocaste: `Funcion`, `Reserva`, `SistemaReservas`, `Main`, `Cliente`.
- **descripción:** en imperativo y en minúsculas ("aplica", "separa", "elimina"), sin punto final, de 72 caracteres como máximo.
- **(PX):** el número de problema del README, cuando aplique.
- **Refs #N:** el número del issue, en el cuerpo del commit.
- Para refactors, nombra la técnica **igual que en la actividad**.

**Ejemplos:**

```
refactor(Funcion): aplica Separate query from modifier en ocupar() (P9)

Refs #3
```

```
refactor(SistemaReservas): aplica Rename method hacer() -> reservar() (P1)

Refs #6
```

```
fix(SistemaReservas): valida todos los asientos antes de ocupar (bug F7)

Refs #7
```

```
docs: agrega diagrama de clases original

Refs #2
```

**Reglas:**
- ✅ Un commit = una técnica = compila y la salida es idéntica.
- ❌ Nada de commits como "cambios", "fix", "ya quedó", "asdf".
- ❌ No subas `.class`, `salida_actual.txt` ni archivos del IDE (ya están en `.gitignore`).

### Comentarios en el código (obligatorios por la actividad)

Encima de cada cambio:

```java
// TÉCNICA UTILIZADA: Separate query from modifier
// REFACTOR: Al aplicar esta técnica se realizaron los siguientes cambios:
//   - ocupar() se dividió en estaLibre() (consulta) y marcarOcupado() (modificador).
```

Si decides **no** aplicar una técnica en algún punto, justifícalo igual con un comentario.

## 4. Pull Requests

- **Título:** el mismo que el issue, por ejemplo `ISS-03 · Refactorizar Funcion.java`.
- **Descripción:** se llena sola con la plantilla. Incluye `Closes #N` para que el issue se cierre al hacer merge.
- **Revisor:** asigna a **@Luis-villanueva-valdivia** (también se agrega solo por `CODEOWNERS`).
- **Merge:** se requiere **mínimo 1 aprobación de Luis**. Usa **"Create a merge commit"**, **no "Squash"**, para que se conserve un commit por técnica.
- Si te piden cambios, haz commits nuevos en la misma rama; no abras otro PR.
- Si `main` avanzó y hay conflictos, actualiza tu rama:
  ```bash
  git checkout main && git pull origin main
  git checkout tu-rama && git merge main
  ```

## 5. Orden de las fases

| Fase | Issues | Cuándo |
|---|---|---|
| 0 · Preparación | #1, #2 | Primero. **#1 es la referencia de todos.** |
| 1 · Clases de apoyo | #3, #4, #5 | En paralelo, después de #1 |
| 2 · Firma de reservar | #6 | Cuando #3, #4 y #5 estén en `main` |
| 3 · Errores y bug | #7 | Cuando #3 y #6 estén en `main` |
| Entrega final | #8, #9 | Al final. URL en la tarea **antes de las 22:00** |
