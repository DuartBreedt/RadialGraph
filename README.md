# RadialGraph
A library for creating customisable, animated radial graphs for the use in your Android app.

This library aims to be a fully customisable radial graph to suit all your needs. If there is a feature you feel is
 missing or needs to be improved upon please reach out or feel free to contribute.
 
*Note:* This library only provisions for a radial graph composed of one or multiple head-to-tail stroked paths as
 shown Below:
 
 - *TODO*: Example image of graph  

### Features
#### Graph
- Composed of `n` Sections
- Uses a data model to programmatically determine the graph's composition of Sections
- The colors of the Sections are customisable (see usage)

#### Labels
- Toggleable labels
- Can be the percent value of the graph which the Section - that the label describes - comprises
- Can define custom text
- The color is currently defined by the color of the Section which the label describes

#### Animation
- The direction of the animation can be defined as CLOCKWISE or COUNTERCLOCKWISE

### Usage
#### Components
- *TODO:* Describe the usable components of the library

#### Examples
- *TODO:* Show full practical examples of the usage

### TODO
#### Animation
- Options such as each graph value cascading in one after the other (attr)
- Toggle animation (attr)
- Editable interpolation
- Editable duration

#### Graph style
- Rounded end (attr)
- Stroke thickness
- Graph size
- Customizable graph end icon

#### Graph labels
- Global color (attr)
- Customisable individual label color
- Icon as labels
- Percent precision/decoration
- Use value as label
    - Decoration for currency
- Animate in once the graph has animated in
- Indicators
    - None
    - Line
    - Dashed line
    - Nodule line
    - Dashed nodule line

#### Features
- Graph key/legend convenience methods
- Render graph portions by descending value, ascending value, or using a separate priority value 
 