# La unidad 3 se aborda de manera práctica. El ejemplo desarrollado en clase está aquí:

Ejemplo: https://github.com/EPERS-UNQ/unidad3-ejemplo-hibernate

## Otro ejemplo de hibernate
Como adjunto hay un ejemplo básico de uso de Hibernate

Ejemplo de una referencia entre Objetos. El Equipo tiene un Técnico. (El técnico podría estar en muchos equipos o en ninguno)

### Usando annotations:

@ManyToOne

```private Tecnico tecnico;```
Usando hbm.xml (en Equipo.hbm.xml):

```<many-to-one name="tecnico" />```
### Ejemplo de un set: Equipo tiene un set de Jugador. El id del equipo en la tabla de Jugador se llama "equipo"

Usando annotations (en Equipo.java):
```
@OneToMany(mappedBy="equipo")
private Set<Jugador> jugadores;
(en Jugador.java):
@ManyToOne
private Equipo equipo;
```
Usando hbm.xml (en Equipo.hbm.xml):

```
<set name="jugadores">
    <key column="equipo" />
    <one-to-many class="Jugador" inverse="true" />
</set>
```
(en Jugador.hbm.xml)

```<many-to-one name="equipo" />```
### Ejemplo de una lista 
la clase FormacionStrategy tiene un atributo "posiciones" de tipo Posicion. La tabla intermedia de relación es PosicionFormacionStrategy, la cual no solo contiene una referencia a Posicion y FormacionStrategy sino un entero representando el orden absoluto de cada tupla.

```
<list name="posiciones" table="PosicionFormacionStrategy" >
     <key column="formacionStrategy"></key>
     <index column="indice" />
     <element column="posicion"/>
</list>
```

### Inverse

Cuando se mapea una relación bidireccional es necesario notificar a hibernate de que ambos atributos corresponden semánticamente a dos extremos de la misma relación. Para resolver esto hibernate usa un atributo "inverse" (o mappedBy en annotations) que significa "quién manda".

Si bien parece un concepto complicado para resolver problemas que no habría que tener, el hecho de decir "quien manda" hace que en los cascades las queries que se ejecuten sean distintas. Por ejemplo: si inverse es false, hibernate inserta el jugador con un null en su equipo, luego inserta al equipo, obtiene el id para racing y tira un update en jugador ingresando el id de Racing en su equipo. Esto tiene algún costo de performance pero además impide tener una validación en la base de datos de que el equipo sea distinto de null.

Si inverse es true, hibernate inserta el Equipo y luego inserta al jugador con el id que corresponde al equipo.

### Mapeo de un enum: 
No es un mapeo ordinario, se usa un CustomType para convertir los valores. El la tabla se genera un campo de tipo numérico

    <typedef name="Posicion" class="org.hibernate.type.EnumType">

        <param name="enumClass">ar.edu.unq.edp.domain.Posicion</param>

    </typedef>

Para generar una relación con una posición se mapea como un tipo básico

       <property name="posicion" type="Posicion" />

### Ejemplo de una jerarquia mapeada en la misma tabla. 
La columna usada como discriminadora es "type"

    <class name="ClasePadre" table="ClasePadre">

        <id name="id">

            <generator class="native"/>

        </id>

        <!-- mapeo de cosas comunes -->

        <discriminator column="type" type="string"/>

        <subclass name="ClaseHija" discriminator-value="ClaseHija">

            <-- mapeo de cosas especificas -->

        </subclass>

    </class>

### Cascade

Hibernate no puede persistir un objeto que tiene referencia a otro objeto transiente ya que no sabría que id poner en ese lugar.

Para evitar tener que tener un conocimiento del estado de todos los objetos del grafo, se puede definir reglas de cascada. Es decir: "Si persisto este objeto, quiero que los objetos referenciados por esta relación también se persistan".

El cascade es un atributo de la relación, y se completa con una lista de valores separados por coma para cada una de las operaciones que se pueden ejecutar sobre la Session

por ejemplo:

        <set name="habilidades" cascade="persiste, save, merge-update" inverse="true">

Si se quiere usar cascade para todas las opciones, se puede hacer

        <set name="habilidades" cascade="all" inverse="true">

Existe un estilo particular de cascade usada para las relaciones de collecciones, que es el "delete-orphan". Eso significa que si elimino un objeto de la colección, se disparará un delete sobre el elemento.

        <set name="habilidades" cascade="all, delete-orphan" inverse="true">

### Isolation

El nivel de aislamiento entre distintas sesiones de hibernate se produce en 2 niveles:

1) El nivel de aislamiento configurado en el driver jdbc

2) La cache de identidad (cache de nivel 1) Hace que un objeto que fue traido a memoria no se modifique por cambios en otras sesiones

Para configurar el nivel de isolation de jdbc, se usa la propiedad hibernate.connection.isolation en el archivo hibernate.cdg.xml

        <!--     Nombre de constante                                    valor     dirty reads  non-repeteable reads phantom reads

            TRANSACTION_READ_UNCOMMITTED           1           si               si                            si

            TRANSACTION_READ_COMMITTED                2           no              si                            si

            TRANSACTION_REPEATABLE_READ              4           no               no                          si

            TRANSACTION_SERIALIZABLE                        8           no              no                          no

         -->

        <property name="hibernate.connection.isolation">4</property>

### Lockeos

Si nada se dice acerca de lockeos, dos usuarios que están modificando a la vez el mismo objeto podrían tener problemas, ya que el último que comitea pisa los cambios realizados por el anterior. Hibernate permite tener lockeos pesismistas y optimistas.

El lockeo pesimista implica que nadie mas puede estar trabajando con ese objeto mientras un usuario está trabajando. Tiene un alto costo de performance que generalmente no se justifica ya que son pocos los casos en una aplicación de gestión dónde se están modificando ambos objetos.

El lockeo optimista permite tirar una excepción al segundo usuario para indicarle que realizó cambios sobre una versión desactualizada de su objeto. Esta es la mejor estrategia para aplicaciones de gestión ya que no es muy costosa a nivel de performance, evita el problema de que un usuario crea que su operación se realizó con éxito cuando no fue así, y además la cantidad de veces que ocurre este problema no es significativo.

Siempre hay que tener presente que las transacciones deben ser lo más cortas posible para evitar estos problemas.

Hibernate permite realizar lockeo optimista usando una version (un atributo numérico o un date en los objetos) o fijándose en las columnas que fueran modificadas, para lo cual el mapeo tiene que permitir "dynamic-update"(es decir, que hibernate no precompile las queries de update y las genere dinámicamente para cada caso con las columnas que necesita).

Hay que tener cuidado con el nivel de isolation elegido para el jdbc, ya que si se usa serializacion, el lockeo optimista podría fracasar ya que la base no permite tener dos transacciones operando sobre el mismo registro.

Ejemplo de lockeo oprtimista por version:

     <class name="Habilidad" table="Habilidad" optimistic-lock="version" >

Ejemplo de lockeo optimista por dirty

    <class name="Habilidad" table="Habilidad" optimistic-lock="version" dynamic-update="false">

Mas material:

Otro ejemplo de hibernate: https://sites.google.com/site/estrategiasdepersistencia/deprecated/hibernate

Documentación oficial de hibernate - http://hibernate.org/orm/documentation/5.2/ (ver Getting Started Guide y User Guide como referencia)
