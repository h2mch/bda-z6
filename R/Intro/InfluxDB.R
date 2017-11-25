install.packages("influxdbr")
library(dplyr)
library(influxdbr)
library(xts)

#Connect to influxdb
con <- influx_connection(host = 'docker')
create_database(con = con, db = "myRDB")

# list all databases
show_databases(con = con)

## Create Data and insert it.
# write example xts-object to database
data("sample_matrix")
# create xts object
xts_data <- xts::as.xts(x = sample_matrix)
#> Warning in strptime(xx, f <- "%Y-%m-%d %H:%M:%OS", tz = tz): unknown
#> timezone 'zone/tz/2017c.1.0/zoneinfo/Europe/Berlin'

# assign some attributes
xts::xtsAttributes(xts_data) <- list(info = "SampleDataMatrix",
                                     UnitTesting = TRUE, 
                                     n = 180)

# print structure to inspect the object
str(xts_data)

influx_write(con = con, 
             db = "myRDB",
             x = xts_data, 
             measurement = "sampledata_xts")


# =====> See more: https://github.com/dleutnant/influxdbr
