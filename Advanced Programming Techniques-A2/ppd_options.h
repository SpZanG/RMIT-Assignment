/***********************************************************************
 * COSC1076 - Advanced Programming Techniques
 * Semester 2 2016 Assignment #2
 * Full Name        : Siyu Zang (Harold)
 * Student Number   : S3534987
 * Course Code      : COSC1076
 * Program Code     : EDIT HERE
 * Start up code provided by Paul Miller
 * Some codes are adopted here with permission by an anonymous author
 ***********************************************************************/

#include "ppd_main.h"
/**
 * The length of money input in the system 
 **/
#define MONEY_BUFFER 5
/**
 * The length of option input in the system 
 **/
#define OPT_BUFFER 200
/**
 * @file ppd_options.h declares all functions to implement the menu
 * options that can be called from the main menu. 
 **/
#define PUR_BUFFER 100
/**
 * This option allows the user to request a display of the items 
 * available for purchase. This is the data loaded into the linked 
 * list in the requirement 2.  
 **/
BOOLEAN display_items(struct ppd_system *);

/** This option allows the user to purchase an item from the menu.
 * This function is called from the main menu when the user has
 * finally decided to purchase an item. This function implements 
 * requirement 5 of the assignment specification. 
 **/ 
BOOLEAN purchase_item(struct ppd_system *);

/** 
 * You must save all data to the data files that were provided on the
 * command line when the program loaded up. This function implements 
 * requirement 6 of the assignment specification.
 **/ 

BOOLEAN save_system(struct ppd_system *);

/**
 * This option adds an item to the system. This function implements 
 * requirement 7 of of assignment specification. 
 **/
BOOLEAN add_item(struct ppd_system *);

/**
 * Remove an item from a category and delete it from the system,
 * including free memory that is no longer being used. 
 * This function implements requirement 8 of the assignment 
 * specification
 **/
BOOLEAN remove_item(struct ppd_system *);

/**
 * This option will require you to iterate over every stock in the
 * list and set its 'on hand' count to the default value specified in
 * the startup code. 
 * This function implements requirement 9 of the assignment 
 * specification
 **/
BOOLEAN reset_stock(struct ppd_system *);

/**
 * This option will require you to iterate over every coin in the coin
 * list and set its 'count' to the default value specified in the
 * startup code. 
 * This requirement implements part 3 of requirement 18 in the 
 * assignment specifications.
 **/
BOOLEAN reset_coins(struct ppd_system *);

/**
 * This option will require you to display the coins as follows. In
 * particular, the counts of coins should be correctly aligned, and
 * they should be sorted from lowest to highest value: 
 * This function implements part 4 of requirement 18 in the assignment
 * specifications.
 **/
BOOLEAN display_coins(struct ppd_system *);


BOOLEAN abort_program(struct ppd_system *);
