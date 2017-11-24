# Varibale
## Asignment
x <- 12/2
## Output
x

# Sequence
seq(1, 10)

##Char Sequence
x <- "hello world"

y <- seq(1, 10, length.out = 5)
y


install.packages("tidyverse")
library(tidyverse)

ggplot(data = mpg) + 
  geom_point(mapping = aes(x = displ, y = hwy, color = "blue"))

################

install.packages("nycflights13")

View(nycflights13::flights)

jan1 <- filter(nycflights13::flights, month == 1, day == 1)
View(jan1)

novDec <- filter(nycflights13::flights, month %in% c(11, 12))

# Flights with a Delay from more than 2 hours
twoHours <- 120;
delayedMoreThan2Hours <- filter(nycflights13::flights, (sched_dep_time + twoHours < dep_time) )
View(delayedMoreThan2Hours)
delayedMoreThan2Hours

#Sorting
arrange(nycflights13::flights, year, month, day)


#Selecting some columns
select(nycflights13::flights, year, month, day)


flights_sml <- select(nycflights13::flights, 
                      year:day, 
                      ends_with("delay"), 
                      distance, 
                      air_time
)
View(mutate(nycflights13::flights,
       gain = arr_delay - dep_delay,
       speed = distance / air_time * 60
))

