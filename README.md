# NookureStaffAPI
Utility for easy access to NookureStaff API.
\
**This plugin does NOT do anything by itself.**\
The plugin contains required classes for some of my plugins.\
\
If any dev want to depend on it too, feel free to let me know.

:warning: **Install it only if the plugin requires it for additional features.**

## Why this exists?
The main reason for creating this plugin was that NookureStaff plugin use Paper event AsyncChatEvent\
which is called AFTER Spigot event AsyncPlayerChatEvent causing that cancelling the paper event\
for private chats like staff chat or similar not take any effect in Spigot event actions by other plugins\
as they are performed first.\
\
For this reason this plugin provides an easy way to access the API of NookureStaff without having\
to deal with all injector stuff to get player options like staff chat enabled or vanish enabled.\
\
**Available methods here:** [DeveloperAPI](https://github.com/AlonsoAliaga/NookureStaffAPI/wiki/DeveloperAPI)

## Resources
- [Spigot resource (Not released)](https://github.com/AlonsoAliaga/NookureStaffAPI/)
- [Download](https://github.com/AlonsoAliaga/NookureStaffAPI/releases)
- [Wiki](https://github.com/AlonsoAliaga/NookureStaffAPI/wiki)
- [Issue tracker](https://github.com/AlonsoAliaga/NookureStaffAPI/issues)
- [Donate](https://paypal.me/AlonsoAliaga)
- [bStats](https://bstats.org/plugin/bukkit/NookureStaffAPI/24958)
- [How to depend on it?](https://github.com/AlonsoAliaga/NookureStaffAPI/wiki/How-to-use)

## Plugin statistics
![bStats.org](https://bstats.org/signatures/bukkit/NookureStaffAPI.svg)

# :warning: NookureStaffAPI is NOT affiliated to NookureStaff plugin, errors here should not be reported to them.
