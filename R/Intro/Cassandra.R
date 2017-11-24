install.packages("RCassandra")
library(RCassandra)
connection <- RC.connect("docker", 9042)

table <- RC.read.table(connection,"blocktimetotxsumamount")

