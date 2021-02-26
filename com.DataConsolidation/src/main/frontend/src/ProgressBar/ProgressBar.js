import React, { Component } from 'react';
import styles from "./ProgressBar.module.css"
import Filler from "./Filler"
import { backendBaseUrl } from '../config';

class ProgressBar extends React.Component {

    constructor(props){
        super(props);
        this.state = {
            percentage: 0,
            showbar: true,
            status: this.props.status, // TODO deal with this
        };
        this.eventSource = new EventSource(`${backendBaseUrl}/Progress`);
        // TODO fix this (same issue as above)
        // this.showbar = true; //should be fed from props but somehow doesnt work
    }

    componentDidMount(){
        this.eventSource.onmessage = e => this.UpdatePercentage(JSON.parse(e.data));
    }

    UpdatePercentage(data){
        console.log(data/15281) // this is raw no of rows, more work
        this.setState({
            percentage: data/15281 * 100,
            showbar: true,
        });
        if (data === 15281) {
            this.setState({
                status: "Successfully consolidated",
                showbar: false,
            });
            this.eventSource.close();
        }
    }

    render() {
        return ( // TODO get rid of h4 surrounding progress bar
            <div>{this.state.showbar ?
                <h4 className={styles.progressBar}>
                    <Filler percentage={this.state.percentage}></Filler>
                </h4>
                : <h4 className={styles.statusMessage}>{this.state.status}</h4>
            }</div>
        );
    }
}

export default ProgressBar;
