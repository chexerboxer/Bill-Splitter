# **Accessibility Report**

## Examining the 7 Universal Design Principles
Due to time restrictions, certain design principles were prioritized over others; all of our decisions will be explained below in addition to relevant features. In general, because the program allows users to create and manage objects, it was important to consider how to make this process as intuitive as possible, while still providing enough freedom with control. As such, we made sure to prioritize principles that emphasize simplification.

**Equitable Use: The design is useful and marketable to people with diverse abilities**

When designing the UI, we kept it grayscale to keep it color-blind friendly. We also kept it minimalistic and used standard fonts to improve clarity for users who may be easily distracted or unfamiliar with navigating technology. Certain physical disabilities we assumed would be addressed through unique hardware the user already has available (ie. sip-and-puff for those unable to use a mouse or keyboard, or a text-to-speech widget for the visually impaired). As for security, in the future, we’d want to use a separate authentication service like Clerk to make sure other users can’t change others’ passwords.

**Flexibility in Use: The design accommodates a wide range of individual preferences and abilities**

This principle can refer to both hardware and user behaviour. The hardware aspect can only be supported with multiple implementations like a web or mobile application, which is beyond the scope of this project. It’s worth noting, however, that this program would ideally have a camera to make the process of uploading receipts easier for users. Nevertheless, we elected to prioritize data processing and displaying as it’s our application’s core feature. However, in case the user wants to input details manually, we made it so they can edit and add items within a bill’s display.

**Simple and Intuitive Use: Use of the design is easy to understand, regardless of the user’s experience, knowledge, language skills, or current concentration level**

Due to the nature of creating and managing objects, we showed only necessary information to users at given views. For example, the dashboard view serves as a filter to the details stored on each bill to not overwhelm users. Furthermore, it follows recognizable IA conventions, such as a sidebar for navigation and interactable “cards” for different objects. This is supported by subtle prompting via hover effects to show users what they can interact with and provides instant feedback on any changes they make to bills by refreshing the view after the interaction.

Another aspect of this principle is the program behaving as expected. Most of this comes down to code logic, and we made sure to test the program to make it robust under different inputs. The image recognition API may produce some errors when transcribing information, but we address that in principle 2 by allowing users to edit items.
The API, in addition to the signup system, can also process unique characters from other languages. With more time, the program could have an option to let the user change between a variety of languages.

It’s also worth noting that using a .csv file as a database means that a username can’t include periods, dashes or semicolons. With more time, a different database implementation can be implemented to work around this, while also making it remote. This way, changes aren’t just stored locally on a user’s device and enforce predictable use by updating everyone’s account.

**Perceivable Information: The design communicates necessary information effectively to the user, regardless of ambient conditions or the user’s sensory abilities**

For the scope of this project, tacit and audio-based cues for the visually impaired we assumed would be addressed by the user’s own setup. In the future, a setting for audio transcription could be included in a setting panel to ease this responsibility. As for redundant cues, we decided their additions would make the program harder to navigate/more overwhelming, so we chose not to implement any more than necessary. Other aspects of this principle are addressed above in principle 3.

**Tolerance for Error: The design minimizes hazards and the adverse consequences of accidental or unintended actions**

Within individual bill views, the user is always able to edit any additions they make in case the image recognition API makes a mistake or they want to add something. In the future, an undo function could be added to reverse edits. However, the most hazardous operation in this program is deleting bills via the dashboard. To undo potential deletions, deleted bills from the dashboard could be stored on a separate page in case the user needs to restore it, and be truly deleted after a week. There are also preventative methods that could be taken, such as using colour to highlight deleting or providing an additional confirmation dialogue to delete bills.

**Low Physical Effort: The design can be used efficiently and comfortably and with a minimum of fatigue**

For a piece of software, the aim of this principle would be to make navigation simple and intuitive, which is addressed above in principle 3.

**Size and Space for Appropriate Use: Appropriate size and space is provided for approach, reach, manipulation, and use regardless of user’s body size, posture, or mobility**
To not exclude certain users who have certain visual impairments or those who have trouble with fine motor control, it would be best for our app to be able to resize its text and components with a fully responsive UI. As of now, only panels like the bill cards on the dashboard can be enlarged, so in the future, a setting panel could include an option to adjust font size and enlarge buttons.


As is with programs involving multiple users, many additional features could be implemented, ie. with how users join bills and manage them together if multiple people have the option to delete or edit information without the others knowing. We decided for the purposes of this project to keep this app to the features it has now, to emphasize its unique functionality.




## Examining Target Audiences
The original vision for this program was for users who go out to eat with groups to use it as a quick way to split their meals. While this isn’t as realistic since we weren’t able to make a mobile app, if we were to polish this app and develop it using a different tech stack, this would be our primary target audience. With this in mind, we’d also want to market it as a way to manage any complex financial splits, such as groceries between roommates. In general, such lifestyles are most common among young adults, but we still designed this program with a wide range of users in mind, given our limitations. In the future, a program like this may even find more popularity with restaurants as an API they can integrate into contactless payment systems, like Square, for cases where they need to serve large groups.




## Examining Undermined Demographics
As mentioned previously, we imagine this app would be more relevant to young adults who have to manage expenditures in groups. As a result, we don’t expect many of our users to be over the age of 65+, purely out of circumstance. However, there are certain reasons why a user may choose to avoid our program because of its design. Those who are visually impaired or otherwise physically impaired may not use this app due to the lack of built-in accessibility aids or the ability to customize the app’s appearance. Needing to upload files via the user’s computer may confuse certain demographics at the sudden change in interfaces, such as the elderly or anyone unfamiliar with technology.
