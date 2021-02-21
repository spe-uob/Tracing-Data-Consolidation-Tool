import React, { Component} from 'react';
import "./ProgressBar.css"
import Filler from "./Filler"
class ProgressBar extends React.Component {
    constructor(props){
        super(props);
        /*Todo : GET APIs to get percentage into state from backend */
        this.state = {
            percentage: 20
        }
    }
    render() {
        return(
            <div className = "Progress-bar">
                <Filler percentage = {this.state.percentage}></Filler>
            </div>
        )
    }
    
}
export default ProgressBar;