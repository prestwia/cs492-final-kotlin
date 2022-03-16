# GIBBET 
In this project we have a custom rendition of the hangman game. 

## General API description
Our app takes makes multiple API calls to the Wordnik API. The user can set a predetermined maximum length that can be adjusted by the user in settings. Also there is a word of the day mode that the user can play. 

## Word of the Day
The API call for the word of the day is a call that will always query the word of the day and give the user a chance to solve it.

## The Word Length
The app also boasts a maximum word length that the user can change if they wish to try guessing longer or shorter words.

## The Word Definition
After a player wins or loses a game. They will be offered the opportunity to google what the definition of that word is. This will cause the app to load a google search of the word for its definition. It's really fun and interesting to learn new words this way.

## Settings
There are two major settings. Maximum word length and attempts.

- Maximum word length has a slider from 5 to 10.
- Number of guesses has a slider from 6 to 12.
