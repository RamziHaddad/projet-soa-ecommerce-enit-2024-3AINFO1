# quarkus-getting-started

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/quarkus-getting-started-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

# Indexation de Produits dans Apache Solr avec Quarkus
Ce projet montre comment indexer des produits dans **Apache Solr** et effectuer des recherches via une API REST exposée par **Quarkus**.

Le projet contient des exemples d'indexation manuelle via **curl**, ainsi qu'un service Quarkus pour ajouter des documents à Solr.

## Prérequis

1. **Java 17 ou supérieur** installé.
2. **Maven** installé.
3. **Docker** installé pour exécuter Solr.
4. **Quarkus** et ses extensions configurées.


### 1. **Démarrer Solr via Docker**
docker-compose up -d

### 2. Indexation via curl
1. Ajouter un Document
Utilisez la commande curl pour ajouter un document à Solr. Dans cet exemple, nous ajoutons un produit avec un identifiant 12345 et une description.

curl "http://localhost:8983/solr/products-index/update?commit=true" -d '
[
  {
    "id": "12345",
    "description": "test product description"
  }
]'
### 3. Rechercher un Document par Description
Effectuer une recherche pour trouver le produit que vous venez d'ajouter en recherchant le mot "test" dans la description :

curl "http://localhost:8983/solr/products-index/select?q=description:test&wt=json"
 
### 4. Rechercher un Document par ID
Voici comment rechercher le produit par son identifiant 12345 : 

curl "http://localhost:8983/solr/products-index/select?q=id:12345&wt=json"
### 5. Rechercher Tous les Documents
Pour voir tous les documents indexés dans Solr, utilisez la requête suivante :

curl "http://localhost:8983/solr/products-index/select?q=*:*&wt=json"

#### Indexation avec Quarkus

### 1. Configurer Solr dans Quarkus
Dans le fichier application.properties, configurez l'URL de Solr comme suit :

solr.url=http://localhost:8983/solr/products-index
### 2. Créer un Service Quarkus pour l'Indexation
Créez un service REST dans Quarkus pour indexer des produits dans Solr.
### 3.Tester l'Indexation via Quarkus
Lancez L'application Quarkus avec la commande suivante :

./mvnw compile quarkus:dev

Ensuite,  tester l'indexation via Postman ou curl :

 curl -X POST http://localhost:8080/solr/index \
 -H "Content-Type: application/json" \
 -d '{"id": "12345", "description": "test product description"}'