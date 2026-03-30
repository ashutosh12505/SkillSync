# SkillSync configuration repository (Spring Cloud Config)

This folder mirrors what you should have in your Git remote (for example `https://github.com/ashutosh12505/skillsync-config`). The **config-server** service clones this repository and serves YAML to all microservices.

## How to store these files in Git

1. Create a Git repository (public or private) on your host (for example GitHub).
2. Copy the contents of **this folder** to the **root** of that repository (so `application.yml`, `auth-service.yml`, `api-gateway.yml`, and so on sit at the repo root, not in a subfolder).
3. Commit and push:

```bash
cd skillsync-config-repo
git init
git add .
git commit -m "Add SkillSync centralized configuration"
git remote add origin https://github.com/<your-account>/skillsync-config.git
git branch -M main
git push -u origin main
```

4. Point **config-server** `spring.cloud.config.server.git.uri` at that repository URL (already set in the SkillSync `config-server` project). For a **private** repository, set credentials via **environment variables** (recommended) on the machine that runs config-server — see below.

## Credentials and secrets to keep in this repo

Store the following in these YAML files (values below match your current project; rotate them in production as needed).

| Secret / setting | Where to put it | Purpose |
|------------------|-------------------|---------|
| **JWT signing secret** | `application.yml` → `jwt.secret` | Same value used by **auth-service** (tokens) and **api-gateway** (validation). |
| **MySQL** `username` / `password` | `auth-service.yml`, `user-service.yml`, `mentor-service.yml`, `skill-service.yml`, `session-service.yml`, `review-service.yml` → `spring.datasource` | JDBC to each database. |
| **RabbitMQ** `username` / `password` | `session-service.yml`, `notification-service.yml` → `spring.rabbitmq` | AMQP clients. |
| **Gmail SMTP** `username` / `password` | `notification-service.yml` → `spring.mail` | Sending email (app password if using Gmail). |
| **Git clone** username / token | **Not** in this repo: use env vars on **config-server** | Cloning a **private** `skillsync-config` repo. See below. |

Optional shared defaults (Eureka URL, Zipkin) live in `application.yml` and are merged into every service.

## Private GitHub repo: authenticate config-server (do not commit tokens into YAML)

Set environment variables when running **config-server** (or use your platform’s secret store):

- `SPRING_CLOUD_CONFIG_SERVER_GIT_USERNAME` — GitHub username or `token`
- `SPRING_CLOUD_CONFIG_SERVER_GIT_PASSWORD` — personal access token (classic) with `repo` scope, or fine-grained token with read access to that repository

Spring Boot maps these to `spring.cloud.config.server.git.username` and `password`. You can also set `GIT_USERNAME` / `GIT_PASSWORD` if you wire them in `config-server`’s `application.yml` (the SkillSync project uses optional env placeholders).

## Encryption (optional, advanced)

For stronger protection than plain YAML in Git, use [Spring Cloud Config encryption](https://docs.spring.io/spring-cloud-config/reference/server.html#_encryption_and_decryption) (`{cipher}...` values) and run the config server with a symmetric or asymmetric key. Plain storage in a **private** repository plus PAT for clone is the usual first step.

## Refresh

After you push changes to this Git repo, restart **config-server** (or trigger a refresh if you add Spring Cloud Bus later). Client apps load remote config at startup; for runtime refresh you would add `/actuator/refresh` and `@RefreshScope` where needed.
