import React, { Component } from 'react';
import "./ProgressBar.css"

class Filler extends React.Component {
    constructor(props){
        super(props);
    }

    render() {
        const width = this.props.percentage;
        return (
            <div className = "Filler" style = {{width : `${width}%`}}/>
        );
    }
}

export default Filler;
