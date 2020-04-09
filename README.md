# HEE Content Management System

The HEE content management and delivery system used to manage and deliver the website at https://www.hee.nhs.uk

## Continuous Integration/Continuous Deployment Status (CI/CD)

![](https://github.com/Manifesto-Digital/hee-content-management-system/workflows/CI/badge.svg)
![](https://github.com/Manifesto-Digital/hee-content-management-system/workflows/CI%20%26%20CD%20%5BDEV%5D/badge.svg)
![](https://github.com/Manifesto-Digital/hee-content-management-system/workflows/CI%20%26%20CD%20%5BTEST%5D/badge.svg)

## Built With

* [BloomReach DXP 14.0.0-2](http://www.bloomreach.com) - BloomReach is the content management system platform used in this project
* [brCloud](https://www.bloomreach.com/en/products/experience-manager/cloud-cms) - Bloomreach Cloud Managed Hosting

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

In order to develop on this platform you will need to have the following tools installed and configured

- IntelliJ IDEA
- Docker
- Java 1.8
- Maven >= 3.5.0 (For `Maven CI Friendly Versions` Support)


### Installing

In order to get a development environment up and running you will need to work through the following steps

Cloning the platform from source control

```
git clone https://github.com/Manifesto-Digital/hee-content-management-system.git
```

To run the brXM project in a docker container, you must install the project, build the docker image and run the docker image respectively.

First install the project:

```
mvn clean install
```

Then build the brXM docker image:

```
mvn -Pdocker.build
```

This maven profile will create a docker image and add it to the local docker registry. The new image will be tagged in `<group_id>/<artifactId>:<version`. Example: `uk.nhs.hee.web/hee-web:0.1.0-SNAPSHOT`

To run the image with in-memory h2 database:

```
mvn -Pdocker.run
```

Running with an embedded MySQL database. To create & run environment containing builtin MySQL DB just run:

```
mvn -Pdocker.run,docker.mysql
```

As a result, default db credentials will be used (admin/admin) and DB name will be the same as project's artifactId (e.g. myproject)

Running with an embedded PostgreSQL database. To create & run environment containing builtin PostgreSQL DB just run:

```
mvn -Pdocker.run,docker.postgres
```

As a result, default db credentials will be used (admin/admin) and DB name will be the same as project's artifactId (e.g. myproject)

To run the image with an external mysql database, add the provided database name, username and password below to the properties
section of your project's pom.xml:

```
    <docker.db.host>DATABASE_HOSTNAME</docker.db.host>
    <docker.db.port>DATABASE_PORT</docker.db.port>
    <docker.db.schema>DATABASE_NAME</docker.db.schema>
    <docker.db.username>DATABASE_USERNAME</docker.db.username>
    <docker.db.password>DATABASE_PASSWORD</docker.db.password>
```

Then run:
```
    mvn -Pdocker.run,mysql
```

After running the docker image, application logs will be shown on the terminal window.

After your project is set up, access the CMS at <http://localhost:8080/cms> and the site at <http://localhost:8080/site>. Logs are located in target/tomcat9x/logs.

## Automatic Export

Automatic export of repository changes to the filesystem is turned on by default. To control this behavior, log into <http://localhost:8080/cms/console> and press the "Enable/Disable Auto Export" button at the top right. To set this as the default for your project edit the file ./repository-data/application/src/main/resources/hcm-config/configuration/modules/autoexport-module.yaml

## Running unit tests

You can run the unit tests for the application like so:

```
mvn clean test
```

### Running integration tests

And you can run the integration tests like so:

```
mvn clean verify
```

### And coding style tests

Code style is enforced using checkstyle and uses the out of the box google java style configuration. Checkstyle is run by default on any maven build that compiles source code.

You can if you wish run the code style checks independently using the following syntax:

```
mvn checkstyle:checkstyle
```

## Development Workflow

Development workflows are implemented using [Github Actions](https://github.com/features/actions). Find below workflows that are implemented so far for CI/CD.

### Continuous Integration (CI) Workflow

When a commit had been made to any branch except `develop`/`release/**` or a PR has been made, then Continuous Integration workflow (`.github/workflows/ci.yml`) would be triggered which would essentially compile & test the project.

### Continuous Deployment (CD) Workflows
#### Development Environment [`master` should be replaced with `develop` when ready]
When a commit (essentially merge commits) had been made to `master`, then Continuous Deployment workflow (`.github/workflows/ci-and-cd-dev.yml`) would be triggered which would perform both Continuous Integration & Deployment to brCloud `development` environment.

#### Test Environment (disabled temporarily) [TODO: yet to be tested & verified]
When a commit (essentially merge commits) had been made to `release/**`, then Continuous Deployment workflow (`.github/workflows/ci-and-cd-tst.yml`) would be triggered which would perform both Continuous Integration & Deployment to brCloud `test` environment. This would also deploy the release package onto Github Packages artefact repo.

TODO:

Add notes describing:

- states and transitions
- source control workflow (Git Flow, etc )
- ticket management (JIRA) ?

## Deployment

To build Tomcat distribution tarballs:

    mvn clean verify
    mvn -P dist
      or
    mvn -P dist-with-development-data

The `dist` profile will produce in the /target directory a distribution tarball, containing the main deployable wars and shared libraries.

The `dist-with-development-data` profile will produce a distribution-with-development-data tarball, also containing the repository-data-development jar in the shared/lib directory. This kind of distribution is meant to be used for deployments to development environments, for instance local deployments or deployments to a continuous integration (CI) system. (Initially, this module contains only "author" and "editor" example users for use in testing. Other data must be placed in this module explicitly by developers, for demo or testing purposes, etc.)

See also src/main/assembly/*.xml if you need to customize the distributions.

TODO: Add additional notes about how to deploy this on a live system


## Releases and Versioning

We use [SemVer](http://semver.org/) for versioning. For the releases available, see the [Releases/Tags on this repository](https://github.com/Manifesto-Digital/hee-content-management-system/releases).

## Authors

* **Pat Shone** - *Initial work* - [pat.shone@manifesto.co.uk](mailto:pat.shone@manifesto.co.uk)
