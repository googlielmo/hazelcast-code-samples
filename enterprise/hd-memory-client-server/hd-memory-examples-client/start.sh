mvn package
docker build . -t gcr.io/hazelcast-33/hdclient:latest
docker push gcr.io/hazelcast-33/hdclient:latest
kubectl run hdclient --limits='memory=1Gi' --image=gcr.io/hazelcast-33/hdclient:latest --env="HZ_LICENSE_KEY=UNLIMITED_LICENSE#99Nodes#VuE0OIH7TbfKwAUNmSj1JlyFkr6a53911000199920009119011112151009"
