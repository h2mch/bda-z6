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

nycflights13::flights

