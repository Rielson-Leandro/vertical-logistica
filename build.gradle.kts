	import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

	plugins {
		id("org.springframework.boot") version "2.7.4"
		id("io.spring.dependency-management") version "1.0.14.RELEASE"
		kotlin("jvm") version "1.6.21"
		kotlin("plugin.spring") version "1.6.21"
		kotlin("plugin.jpa") version "1.6.21"
	}

	group = "br.com.rielsonleandro"
	version = "0.0.1-SNAPSHOT"

	java {
		sourceCompatibility = JavaVersion.VERSION_11
	}

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation("org.springframework.boot:spring-boot-starter-web")
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
		implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21")
		implementation("javax.validation:validation-api:2.0.1.Final")

		runtimeOnly("com.h2database:h2")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
		testImplementation ("org.mockito:mockito-core:3.2.0")
		testImplementation ("org.mockito:mockito-junit-jupiter")
		testImplementation ("org.junit.jupiter:junit-jupiter-api")
		testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine")
		testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs += "-Xjsr305=strict"
			jvmTarget = "11"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}