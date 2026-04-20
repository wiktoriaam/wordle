package com.example.wordle.model

import java.io.File

object WordsReader{
    private var wordList: List<String> ?= null

    fun getWordList(filename: String = "/data/words.txt"): List<String>{
        if (wordList != null){
            return wordList!!
        }

        wordList = WordsReader::class.java.getResourceAsStream(filename)?.bufferedReader()?.readLines()

        return wordList!!
    }
}