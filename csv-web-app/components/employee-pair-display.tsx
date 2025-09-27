"use client"

import {useState} from "react";
import {EmployeePair} from "@/types/employee";
import FileUpload from "@/components/file-upload";
import ResultsTable from "@/components/results-table";

export default function EmployeePairDisplay() {
    const [results, setResults] = useState<EmployeePair[]>([])
    const [isLoading, setIsLoading] = useState<boolean>(false)

    const handleUploadSuccess = (data: EmployeePair[]) => {
        setResults(data)
        setIsLoading(false)
    }

    return (
        <div className="space-y-8">
            <FileUpload
                onUploadStart={() => setIsLoading(true)}
                onUploadSuccess={handleUploadSuccess}
                onUploadError={() => setIsLoading(false)}
            />

            {isLoading && <div>Processing...</div>}

            {results.length > 0 && (
                <ResultsTable data={results} />
            )}
        </div>
    )
}