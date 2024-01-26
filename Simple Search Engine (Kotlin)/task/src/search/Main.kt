package search

import java.io.File
import kotlin.system.exitProcess

val index = mutableMapOf<String, MutableList<Int>>()

fun main(args: Array<String>) {

    val peoplesList: List<String> = File(args.last()).readLines()
    peoplesList.mapIndexed { index, s -> index to s }
        .flatMap {
            val lineNum = it.first
            it.second.split(" ").map { lineNum to it.lowercase() }
        }
        .groupByTo(index, { it.second }, { it.first })

    val searcher = Searcher(peoplesList)
    while (true) {
        searcher.menu()
    }

}

fun search(peoples: List<String>) {

    val strategy = userInput("Select a matching strategy: ALL, ANY, NONE")
    val query = userInput("Enter a name or email to search all matching people.").split(" ").map { it.lowercase() }

    val firstList = index[query.first()] ?: setOf()
    val secondList = index[query.last()] ?: setOf()

    when (strategy) {
        "NONE" -> {
            peoples
                .filterIndexed { index, _ -> !firstList.contains(index) && !secondList.contains(index) }
                .forEach(::println)
        }

        "ALL" -> {
            val intersect: Set<Int> = firstList.intersect(secondList.toSet())
            intersect.map { peoples[it] }.forEach(::println)
        }

        "ANY" -> {
            val distinct: List<Int> = firstList.union(secondList).distinct()
            distinct.map { peoples[it] }.forEach(::println)

        }
    }
}


class Searcher(private val data: List<String>) {

    fun menu() {
        val menu = """
            === Menu ===
            1. Find a person
            2. Print all people
            0. Exit
        """.trimIndent()

        while (true) {
            println(menu)
            when (readln()) {
                "0" -> println("Bye!").also { exitProcess(0) }
                "1" -> search(data)
                "2" -> printAll()
                else -> println("Incorrect option! Try again.")
            }
        }
    }

    private fun printAll() {
        println("=== List of people ===\n" + data.joinToString("\n"))
    }

}


fun userInput(input: String): String = println(input).run { readln() }


