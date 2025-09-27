import {EmployeePair} from "@/types/employee";

const API_BASE_URL = process.env.API_BASE_URL || 'http://localhost:8080';

export async function uploadCSV(file: File): Promise<EmployeePair[]> {
    const formData = new FormData();
    formData.append('file', file);

    const response = await fetch(`${API_BASE_URL}/api/v1/csv-upload/employees`, {
        method: 'POST',
        body: formData,
    });

    if (!response.ok) {
        throw new Error(response.statusText);
    }

    return response.json();
}