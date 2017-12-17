
## Windows

hem@zrhn1810 MINGW64 /C/source/CAS-BDA/docker/bitcoind
$ docker build -t h2m/bitcoind .

hem@zrhn1810 MINGW64 /C/source/CAS-BDA/docker/bitcoind
$ docker run -v /docker-share/bitcoin:/home/bitcoin/.bitcoin -it --rm h2m/bitcoind -printtoconsole

## Ubuntu
```bash
heinz@x1-carbon:~/source/bda-z6/docker/bitcoind$ docker build -t h2m/bitcoind .
Sending build context to Docker daemon  5.632kB
Step 1/16 : FROM debian:stretch-slim
...

heinz@x1-carbon:~/source/bda-z6/docker/bitcoind$ docker run -v /media/heinz/Elements/docker-share/bitcoin$:/home/bitcoin/.bitcoin -it --rm h2m/bitcoind -printtoconsole

```


