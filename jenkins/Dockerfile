FROM jenkins/jenkins:lts-jdk21

USER root

# Установка зависимостей
RUN apt-get update && apt-get install -y \
    git \
    maven \
    curl \
    unzip \
    ca-certificates \
    gnupg \
    lsb-release \
    && rm -rf /var/lib/apt/lists/*

ENV HELM_VERSION=v3.14.4

# Установка kubectl
RUN curl -LO "https://dl.k8s.io/release/$(curl -Ls https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl" && \
    install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl && \
    rm kubectl && \
    # Установка Docker CLI
    curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg && \
           echo \
           "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/debian \
           $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null && \
           apt-get update && apt-get install -y docker-ce-cli && \
    # Установка Helm
    curl -fsSL https://get.helm.sh/helm-${HELM_VERSION}-linux-amd64.tar.gz | \
        tar -xz && \
        mv linux-amd64/helm /usr/local/bin/helm && \
        rm -rf linux-amd64

RUN helm repo add bitnami https://charts.bitnami.com/bitnami && \
    helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx && \
    helm repo update

# Установка плагинов Jenkins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

# Инициализация groovy-скриптов
COPY init.groovy.d /usr/share/jenkins/ref/init.groovy.d/

USER jenkins
ENV PATH="/usr/local/bin:$PATH"
