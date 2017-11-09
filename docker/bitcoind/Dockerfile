FROM debian:stretch-slim

MAINTAINER Heinz Marti <github@h2m.ch>

ENV GOSU_VERSION=1.9
ENV BITCOIN_VERSION=0.15.0.1
ENV BITCOIN_FOLDER_VERSION=0.15.0
ENV BITCOIN_DATA=/home/bitcoin/.bitcoin
ENV PATH=/opt/bitcoin-${BITCOIN_FOLDER_VERSION}/bin:$PATH

RUN useradd -r bitcoin

RUN apt-get update -y \
  && apt-get install -y curl gnupg \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# Install GOSU
RUN curl -o /usr/local/bin/gosu -fSL https://github.com/tianon/gosu/releases/download/${GOSU_VERSION}/gosu-$(dpkg --print-architecture) \
	&& chmod +x /usr/local/bin/gosu

# Install Bitcoin Client
RUN curl -SLO https://bitcoin.org/bin/bitcoin-core-${BITCOIN_VERSION}/bitcoin-${BITCOIN_VERSION}${BITCOIN_BRANCH}-x86_64-linux-gnu.tar.gz \
  && tar -xzf bitcoin-${BITCOIN_VERSION}${BITCOIN_BRANCH}-x86_64-linux-gnu.tar.gz -C /opt \
  && rm bitcoin-${BITCOIN_VERSION}${BITCOIN_BRANCH}-x86_64-linux-gnu.tar.gz

VOLUME ["/home/bitcoin/.bitcoin"]

COPY docker-entrypoint.sh /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]

EXPOSE 8332 8333 18332 18333 18444

CMD ["bitcoind"]