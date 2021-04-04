package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

const (
	CREW_SIZE        = 200
	ROW_SIZE = 50
	RIGHT            = true
	LEFT             = false
)

func generateCrew() [CREW_SIZE]bool {
	var crew [CREW_SIZE]bool

	for i := 0; i < CREW_SIZE; i++ {
		if rand.Intn(2) == 0 {
			crew[i] = LEFT
		} else {
			crew[i] = RIGHT
		}
	}

	return crew
}

func commandCrew(crew *[]bool, waitGroup *sync.WaitGroup) {
	defer waitGroup.Done()

	var (
		i       = 0
		size    = len(*crew)
		changed = true
	)

	for changed{
		changed = false
		i = 0
		for ; i < size-1; i++ {
			if (*crew)[i] != (*crew)[i+1] {
				(*crew)[i] = !(*crew)[i]
				changed = true
			}
		}
	}
}

func imitateCommand(crew[CREW_SIZE]bool) [CREW_SIZE]bool{

	var waitGroup sync.WaitGroup

	for i :=0; i < CREW_SIZE / ROW_SIZE; i++{
		waitGroup.Add(1)
		crewSlice := crew[i*ROW_SIZE : (i+1)*ROW_SIZE]
		go commandCrew(&crewSlice, &waitGroup)
	}

	waitGroup.Wait()

	return crew
}

func printCrew(crew[CREW_SIZE]bool){
	for i :=0; i < CREW_SIZE / ROW_SIZE; i++{
		fmt.Println(crew[i*ROW_SIZE : (i+1)*ROW_SIZE])
	}
}

func main() {
	rand.Seed(time.Now().UnixNano())

	crew := generateCrew()

	fmt.Println("Before command:")
	printCrew(crew)

	crew = imitateCommand(crew)
	fmt.Println("After command:")
	printCrew(crew)
}
