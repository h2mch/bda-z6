

hem@zrhn1810 MINGW64 /C/source/CAS-BDA/docker/bitcoind
$ docker build -t h2m/bitcoind .

hem@zrhn1810 MINGW64 /C/source/CAS-BDA/docker/bitcoind
$ docker run -v /docker-share/bitcoin:/home/bitcoin/.bitcoin -it --rm h2m/bitcoind -printtoconsole
