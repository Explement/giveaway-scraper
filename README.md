# 🎁 Giveaway Scraper 🎁
 This Giveaway Scraper was specially made for people who want easy giveaway wins. It's essentially a Reddit filter, using Java streams for filtering and mapping, and uses the Reddit API and Discord API to get and then display the information. 
 # 📝 Setup 📝
  1. First things first, you'd want to head to the Configuration file. Change the 'token' string to your Discord bot token. **I don't recommend changing the subreddit value**, as the code was specifically designed for it. Change the game filter as you please, aswell as the username block filter. 
  2. Go to *redditpost/SendPost* and change **ALL** the credentials, aswell as the strings in the UserAgent. If you don't know how, please lookup a guide on how to make a reddit. Click [here](https://ssl.reddit.com/prefs/apps/) to make the reddit application. 
  3. Assuming you added your Discord bot to your server, all is done. Run the application and the bot is up. 
  It has 2 main commands: 
  - **Search** - a command to search for games, this apps main purpose 
  - **Clear** - clears all messages in a channel 
  # 💻 Contributing 💻 
  Contributing isn't really needed for this project. However, there is a few QOL features that would be pretty nice. Them being: 
  - A new command to search for specific games. This would be greatly appreciated. 
  - A overhaul to the 'Clear' command, as it lacks speed. 
  Git Quickstart:
   1. Make sure that in your terminal your directory is focused on the `giveaway-scraper` 
   2. Run ```git status``` 
   3. If it runs fine, run ```git add .``` or ```git add <files>``` for specific files 
   4. Now run ```git commit -m "Message"``` 
   
   5. Run ```git push origin master``` and it's done

   # 🚀 About  🚀
This project took me 4 days to make, as it was essentially just a revamp of a previous project. This is made to get giveaway links and even post to them without any effort.

It works by going off the subreddit r/steamgiveaways, and scraping the 100 newest posts. It checks the flair, and if it's open, it proceeds to the next step; filtering. It checks each of them for keywords in the filter (which conveniently, is in the config file), and if its found to be good, it's returned to the discord bot which then sends a message for each post found.

This project is likely to be redone, with support for more subreddits, more speed, more commands, and a bigger config file.

   # 📄 License 📄 
   This project has no license as it took me just about a few days to make.
